package com.github.leaderboards.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.lettuce.core.LettuceFutures;
import io.lettuce.core.Range;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.ScoredValue;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;

@Component
public class LeaderboardService {

	@Autowired
	StatefulRedisConnection<String, String> connection;
	
	private String RANK_KEY = "rank";
	
	private Long defaultPageSize = 20L;
	
	public MemberRanked rankMember(Member member) {
		try {
			RedisAsyncCommands<String, String> commands = connection.async();
			commands.multi();
			RedisFuture<Double> score = commands.zincrby(RANK_KEY, member.getScore(), member.getKey());
			RedisFuture<Long> rank = commands.zrevrank(RANK_KEY, member.getKey());
			if( member.getMemberData() != null ) 
				commands.set(RANK_KEY + ":member:" + member.getKey() +":user-info" , member.getMemberData());
			commands.lpush(RANK_KEY + ":member:" + member.getKey() + ":latest-activities", ""+member.getScore() ); // TODO Add more info		
			commands.ltrim(RANK_KEY + ":member:" + member.getKey() + ":latest-activities", 0, 24);
			commands.exec();
			
			return new MemberRanked( member.getKey(), score.get(), rank.get());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public MemberRanked rankFor(String key) {
		try {
			RedisAsyncCommands<String, String> commands = connection.async();
			commands.multi();
			RedisFuture<Long> rank = commands.zrevrank(RANK_KEY, key);
			RedisFuture<Double> score = commands.zscore(RANK_KEY, key);
			commands.exec();
			
			return new MemberRanked( key , score.get(), rank.get());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<MemberRanked> aroundMe(String key, Long pageSize) {
		try {
			Long rank = connection.sync().zrevrank(RANK_KEY, key);

			Range<Long> range = resolveRange(rank, safePageSize(pageSize));
			
			List<ScoredValue<String>> scoresValues = connection.sync()
				.zrevrangeWithScores( RANK_KEY, range.getLower().getValue(), range.getUpper().getValue());
			
			
			RedisAsyncCommands<String, String> commands = connection.async();
			commands.multi();
			
			List<RedisFuture<Long>> futures = new ArrayList<RedisFuture<Long>>();
			
			for( ScoredValue<String> sv : scoresValues ) {
				futures.add( commands.zrevrank(RANK_KEY, sv.getValue()) );
			}
			commands.exec();
			
			LettuceFutures.awaitAll(5, TimeUnit.SECONDS, futures.toArray(new RedisFuture[futures.size()]));
			
			List<MemberRanked> members = new ArrayList<>(scoresValues.size());
			
			for (int i = 0 ; i < scoresValues.size() ; i++ ) {
				ScoredValue<String> sv = scoresValues.get(i);
				RedisFuture<Long> zrevRankFuture = futures.get(i);
				members.add( new MemberRanked(sv.getValue(), sv.getScore(), zrevRankFuture.get()) );
			}
			
			// test if must load user data...
			
			loadUserData(members);
			return members;
			
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	private void loadUserData(List<MemberRanked> members) throws Exception {
		RedisAsyncCommands<String, String> commands = connection.async();
		
		commands.multi();
		
		List<RedisFuture<String>> futures = new ArrayList<RedisFuture<String>>();
		
		for( MemberRanked m : members ) {
			futures.add( commands.get(RANK_KEY + ":member:" + m.getKey() +":user-info") );
		}
		commands.exec();
		
		LettuceFutures.awaitAll(5, TimeUnit.SECONDS, futures.toArray(new RedisFuture[futures.size()]));
		

		for (int i = 0 ; i < members.size() ; i++ ) {
			MemberRanked m = members.get(i);
			String userData = futures.get(i).get();
			m.setUserData(userData);
		}
		
	}

	private Long safePageSize(Long pageSize) {
		return pageSize <= 0L ? defaultPageSize : pageSize;
	}

	private Range<Long> resolveRange(Long rank, Long pageSize) {
		Long lower = rank - (pageSize / 2);
		if (lower < 0) {
			lower = 0L;
		}

		Long upper = (lower + pageSize) - 1;
		return Range.create(lower, upper);
	}
	
}
