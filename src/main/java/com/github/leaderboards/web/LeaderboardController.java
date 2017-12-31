package com.github.leaderboards.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.github.leaderboards.web.resources.LatestActivities;
import com.github.leaderboards.web.resources.LatestActivitiesResourceAssembler;
import com.github.leaderboards.web.resources.LatestActivitiesResourceAssembler.LatestActivitiesResource;
import com.github.leaderboards.web.resources.MemberRanked;
import com.github.leaderboards.web.resources.MemberRankedResourceAssembler;
import com.github.leaderboards.web.resources.MemberRankedResourceAssembler.MemberRankedResource;
import com.github.leaderboards.web.resources.MonthlyScore;
import com.github.leaderboards.web.resources.MonthlyScoreResourceAssembler;
import com.github.leaderboards.web.resources.MonthlyScoreResourceAssembler.MonthlyScoreResource;
import com.github.leaderboards.web.resources.Score;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value="/rank",produces="application/hal+json")
public class LeaderboardController {

	@Autowired
	LeaderboardService leaderboardService;

	@Autowired
	MemberRankedResourceAssembler memberRankedResourceAssembler;
	
	@Autowired
	LatestActivitiesResourceAssembler latestActivitiesResourceAssembler;
	
	@Autowired
	MonthlyScoreResourceAssembler monthlyScoreResourceAssembler;
	
	@PostMapping
	public HttpEntity<?> rank(@Valid @RequestBody Score member, Errors erros, UriComponentsBuilder b) {
		if(erros.hasErrors()) {
			// TODO better errors messages.
			return ResponseEntity.badRequest().body(erros.getFieldErrors());
		}
		leaderboardService.rankMember(member);
		
		UriComponents uriComponents = b.path("/rank/member/{id}").buildAndExpand(member.getUserId());
	    return ResponseEntity.created(uriComponents.toUri()).build();
	}
	
	@GetMapping
	public HttpEntity<?> rank(@RequestParam(name="size",defaultValue="20",required=false) Long pageSize){
		List<MemberRanked> members = leaderboardService.rank(pageSize);
		return ResponseEntity.ok().body(new Resources<>(  memberRankedResourceAssembler.toResources(members) ));
	}
	
	@GetMapping("/monthly")
	public HttpEntity<?> rankMonthly(@RequestParam(name="size",defaultValue="20",required=false) Long pageSize){
		List<MemberRanked> members = leaderboardService.rankMonthly(pageSize);
		return ResponseEntity.ok().body(new Resources<>(  memberRankedResourceAssembler.toResources(members) ));
	}
	
	@GetMapping("/member/{key}")
	public Mono<HttpEntity<MemberRankedResource>>  rank(@PathVariable("key") String key) {
		return leaderboardService.rankFor(key)
				.flatMap( memberRanked -> Mono.just(ResponseEntity.ok(memberRankedResourceAssembler.toResource(memberRanked))));
	}
	
	@GetMapping("/member/{key}/around-me")
	public HttpEntity<Resources<MemberRankedResource>> aroundMe(@PathVariable("key") String key, 
									@RequestParam(name="size",defaultValue="20",required=false) Long pageSize){
		List<MemberRanked> members = leaderboardService.aroundMe(key, pageSize);
		return ResponseEntity.ok(new Resources<>(  memberRankedResourceAssembler.toResources(members) ));
	}
	
	@GetMapping("/member/{key}/actions")
	public HttpEntity<LatestActivitiesResource> actions(@PathVariable("key") String key){
		LatestActivities latestActivities = leaderboardService.actions(key);
		return ResponseEntity.ok(latestActivitiesResourceAssembler.toResource(latestActivities));
	}
	
	@GetMapping("/member/{key}/scores")
	public HttpEntity<MonthlyScoreResource> scores(@PathVariable("key") String key){
		 MonthlyScore monthlyScore = leaderboardService.scores(key);
		 return ResponseEntity.ok(monthlyScoreResourceAssembler.toResource(monthlyScore));
	}
}
