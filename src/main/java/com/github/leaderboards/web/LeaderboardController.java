package com.github.leaderboards.web;

import static org.springframework.http.ResponseEntity.status;

import java.util.List;

import javax.validation.Valid;
import javax.xml.ws.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.github.leaderboards.web.resources.LatestActivities;
import com.github.leaderboards.web.resources.LatestActivitiesResourceAssembler;
import com.github.leaderboards.web.resources.MemberRanked;
import com.github.leaderboards.web.resources.MemberRankedResourceAssembler;
import com.github.leaderboards.web.resources.MonthlyScore;
import com.github.leaderboards.web.resources.MonthlyScoreResourceAssembler;
import com.github.leaderboards.web.resources.Score;

@RestController
public class LeaderboardController {

	@Autowired
	LeaderboardService leaderboardService;

	@Autowired
	MemberRankedResourceAssembler memberRankedResourceAssembler;
	
	@Autowired
	LatestActivitiesResourceAssembler latestActivitiesResourceAssembler;
	
	@Autowired
	MonthlyScoreResourceAssembler monthlyScoreResourceAssembler;
	
	@PostMapping("/rank")
	public ResponseEntity<?> rank(@Valid @RequestBody Score member, Errors erros, UriComponentsBuilder b) {
		try {
			if(erros.hasErrors()) {
				// TODO better errors messages.
				return ResponseEntity.badRequest().body(erros.getFieldErrors());
			}
			leaderboardService.rankMember(member);
			UriComponents uriComponents = b.path("/rank/member/{id}").buildAndExpand(member.getUserId());
		    return ResponseEntity.created(uriComponents.toUri()).build();
		} catch (Exception e) {
			return status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/rank")
	public Resources<?> rank(@RequestParam(name="size",defaultValue="20",required=false) Long pageSize){
		try {
			List<MemberRanked> members = leaderboardService.rank(pageSize);
			return new Resources<>(  memberRankedResourceAssembler.toResources(members) );
		} catch (Exception e) {
			return null;
		}
	}
	
	@GetMapping("/rank/monthly")
	public Resources<?> rankMonthly(@RequestParam(name="size",defaultValue="20",required=false) Long pageSize){
		try {
			List<MemberRanked> members = leaderboardService.rankMonthly(pageSize);
			return new Resources<>(  memberRankedResourceAssembler.toResources(members) );
		} catch (Exception e) {
			return null;
		}
	}
	
	@GetMapping("/rank/member/{key}")
	public Resource<MemberRanked>  rank(@PathVariable("key") String key) {
		MemberRanked memberRanked = leaderboardService.rankFor(key);
		return memberRankedResourceAssembler.toResource(memberRanked);
	}
	
	@GetMapping("/rank/member/{key}/around-me")
	public Resources<?> aroundMe(@PathVariable("key") String key, 
									@RequestParam(name="size",defaultValue="20",required=false) Long pageSize){
		
		try {
			List<MemberRanked> members = leaderboardService.aroundMe(key, pageSize);
			return new Resources<>(  memberRankedResourceAssembler.toResources(members) );
		} catch (Exception e) {
			return null;
		}
	}
	
	@GetMapping("/rank/member/{key}/actions")
	public Resource<LatestActivities> actions(@PathVariable("key") String key){
		try {
			LatestActivities latestActivities = leaderboardService.actions(key);
			return latestActivitiesResourceAssembler.toResource(latestActivities);
		} catch (Exception e) {
			return null;
		}
	}
	
	@GetMapping("/rank/member/{key}/scores")
	public Resource<MonthlyScore> scores(@PathVariable("key") String key){
		try {
			 MonthlyScore monthlyScore = leaderboardService.scores(key);
			 return monthlyScoreResourceAssembler.toResource(monthlyScore);
		} catch ( Exception e) {
			return null;
		}
	}
}
