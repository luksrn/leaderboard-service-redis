package com.github.leaderboards.web;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.leaderboards.LeaderboardException;
import com.github.leaderboards.util.DateRange;
import com.github.leaderboards.web.resources.Activity;
import com.github.leaderboards.web.resources.LatestActivities;
import com.github.leaderboards.web.resources.MemberRanked;
import com.github.leaderboards.web.resources.MonthlyScore;
import com.github.leaderboards.web.resources.Score;
import com.github.leaderboards.web.resources.ScoreDay;

import io.lettuce.core.LettuceFutures;
import io.lettuce.core.Range;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.ScoredValue;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

@Component
public class LeaderboardService {

	final String RANK_GERAL_KEY = "rank";
	final String RANK_MONTH_KEY = "rank:month";
	final String RANK_LOGS_ACTIONS = "rank:logs:actions";
	final String RANK_LOGS_SCORES = "rank:logs:scores";
	final String KEY_USER_INFO = "user-info";
	
	final Long defaultPageSize = 20L;
	
	@Autowired
	StatefulRedisConnection<String, String> connection;
	
	@Autowired
	ObjectMapper jsonMapper;
	
	public void rankMember(Score memberScore)  {
		RedisAsyncCommands<String, String> commands = connection.async();
		commands.multi();
		
		commands.zincrby(RANK_GERAL_KEY, memberScore.getScore(), memberScore.getUserId());
		commands.zincrby( RANK_MONTH_KEY   + ":" + YearMonth.now(), memberScore.getScore(), memberScore.getUserId());
		
		commands.hincrbyfloat( RANK_LOGS_SCORES + ":" + LocalDate.now() , memberScore.getUserId() , memberScore.getScore() );
		
		Activity activity = new Activity( memberScore.getScore() , memberScore.getDescription() , memberScore.getMoment() );
		
		try {
			commands.lpush(RANK_LOGS_ACTIONS  + ":" + memberScore.getUserId(), jsonMapper.writeValueAsString(activity) );
		} catch (JsonProcessingException e) {
			throw new LeaderboardException("Invalid JSON", e);
		}
		commands.ltrim(RANK_LOGS_ACTIONS  + ":" + memberScore.getUserId(), 0, 24);
		
		if( memberScore.getMemberData() != null ) {
			commands.hset( KEY_USER_INFO , memberScore.getUserId() , memberScore.getMemberData());
		}
		commands.exec();
	}
	
	public MemberRanked rankFor(String key) {
		try {
			RedisAsyncCommands<String, String> commands = connection.async();
			commands.multi();
			RedisFuture<Long> rank = commands.zrevrank(RANK_GERAL_KEY, key);
			RedisFuture<Double> score = commands.zscore(RANK_GERAL_KEY, key);
			commands.exec();
			
			return new MemberRanked( key , score.get(), rank.get());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<MemberRanked> rank(Long pageSize) {
		Range<Long> range = resolveRange(0L, pageSize);
		
		List<MemberRanked> members = loadRankByRange(RANK_GERAL_KEY,range);
		
		loadUserData(members);
		return members;
	}
	

	public List<MemberRanked> rankMonthly(Long pageSize) {
		Range<Long> range = resolveRange(0L, pageSize);
		
		List<MemberRanked> members = loadRankByRange(RANK_MONTH_KEY + ":" + YearMonth.now() ,range);
		
		loadUserData(members);
		return members;
	}
	
	public List<MemberRanked> aroundMe(String key, Long pageSize) {
		Long rank = connection.sync().zrevrank(RANK_GERAL_KEY, key);

		Range<Long> range = resolveRange(rank, safePageSize(pageSize));
		
		List<MemberRanked> members = loadRankByRange(RANK_GERAL_KEY,range);
		
		loadUserData(members);
		return members;
	}

	private List<MemberRanked> loadRankByRange(String rankKey, Range<Long> range)  {
		List<ScoredValue<String>> scoresValues = connection.sync()
			.zrevrangeWithScores( rankKey, range.getLower().getValue(), range.getUpper().getValue());
		
		
		RedisAsyncCommands<String, String> commands = connection.async();
		commands.multi();
		
		List<RedisFuture<Long>> futures = new ArrayList<RedisFuture<Long>>();
		
		for( ScoredValue<String> sv : scoresValues ) {
			futures.add( commands.zrevrank(rankKey, sv.getValue()) );
		}
		commands.exec();
		
		LettuceFutures.awaitAll(5, TimeUnit.SECONDS, futures.toArray(new RedisFuture[futures.size()]));
		
		List<MemberRanked> members = new ArrayList<>(scoresValues.size());
		
		try {
			for (int i = 0 ; i < scoresValues.size() ; i++ ) {
				ScoredValue<String> sv = scoresValues.get(i);
				RedisFuture<Long> zrevRankFuture = futures.get(i);
				members.add( new MemberRanked(sv.getValue(), sv.getScore(), zrevRankFuture.get()) );
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new LeaderboardException(e.getMessage(), e);
		}
		return members;
	}
	
	public LatestActivities actions(String key) {
		
		try {
			RedisCommands<String, String> commands = connection.sync();
			
			List<String> actions = commands.lrange(RANK_LOGS_ACTIONS + ":" + key, 0, -1);
			
			List<Activity> result = new ArrayList<>(actions.size());
			for( String action : actions ) {
				result.add(jsonMapper.readValue(action, Activity.class));
			}
			return new LatestActivities(key,result);
		} catch (Exception e) {
			return null;
		}
	}
	
	public MonthlyScore scores(String memberKey) {
		try {
			RedisAsyncCommands<String, String> commands = connection.async();
			
			List<String> keys = commands.keys(RANK_LOGS_SCORES + ":" + YearMonth.now() + "-??").get();
			
			commands.multi();
	
			List<RedisFuture<String>> futures = new ArrayList<RedisFuture<String>>();
			for( String key : keys ) {
				futures.add( commands.hget( key , memberKey ) );
			}
			commands.exec();
			
			LocalDate start = LocalDate.now();
			LocalDate end = start.minusDays(30);
			
			Map<String, ScoreDay> scores= new TreeMap<>();
			
			for (LocalDate d : DateRange.between(end, start)) {
				scores.put(d.toString(), new ScoreDay(d));
			}
			
			LettuceFutures.awaitAll(5, TimeUnit.SECONDS, futures.toArray(new RedisFuture[futures.size()]));
			
			for (int i = 0 ; i < futures.size() ; i++ ) {
				String day = keys.get(i).replaceFirst(RANK_LOGS_SCORES + ":", "" );
				String score = futures.get(i).get();
				scores.compute( day, (key, value ) -> {
					value.setScores(Double.parseDouble(score));
					return value;
				});
			}
			MonthlyScore monthlyScore = new MonthlyScore( memberKey,   scores.values().stream().collect(Collectors.toList()) );
			return monthlyScore;
		} catch ( InterruptedException | ExecutionException e) {
			throw new LeaderboardException(e.getMessage(), e);
		}
	}


	private void loadUserData(List<MemberRanked> members)  {
		RedisAsyncCommands<String, String> commands = connection.async();
		
		commands.multi();
		
		List<RedisFuture<String>> futures = new ArrayList<RedisFuture<String>>();
		
		for( MemberRanked m : members ) {
			futures.add( commands.hget( KEY_USER_INFO,  m.getKey() ) );
		}
		commands.exec();
		
		LettuceFutures.awaitAll(5, TimeUnit.SECONDS, futures.toArray(new RedisFuture[futures.size()]));
		
		try {
			for (int i = 0 ; i < members.size() ; i++ ) {
				MemberRanked m = members.get(i);
				String userData = futures.get(i).get();
				m.setUserData( jsonMapper.readValue(userData, HashMap.class));
			}
		} catch (IOException | InterruptedException | ExecutionException e) {
			throw new LeaderboardException(e.getMessage(), e);
		}
		
	}

	private Long safePageSize(Long pageSize) {
		return pageSize <= 0L ? defaultPageSize : pageSize;
	}

	private Range<Long> resolveRange(Long start, Long pageSize) {
		Long lower = start - (pageSize / 2);
		if (lower < 0) {
			lower = 0L;
		}

		Long upper = (lower + pageSize) - 1;
		return Range.create(lower, upper);
	}


	
}
