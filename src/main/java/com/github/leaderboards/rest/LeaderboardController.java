package com.github.leaderboards.rest;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LeaderboardController {

	@Autowired
	LeaderboardService leaderboardService;
	
	@PostMapping("/rank-member")
	public MemberRanked rank(@RequestBody Member member) {
		return leaderboardService.rankMember(member);
	}
	
	@GetMapping("/rank-member/{key}")
	public ResponseEntity<MemberRanked> rank(@PathVariable("key") String key) {
		MemberRanked memberRanked = leaderboardService.rankFor(key);
		if(memberRanked == null) {
			return notFound().build();
		}
		return ok().body(memberRanked);
	}
	
	@GetMapping("/rank-member/{key}/around-me")
	public ResponseEntity<?> aroundMe(@PathVariable("key") String key, 
									@RequestParam(name="size",defaultValue="20",required=false) Long pageSize){
		try {
			return ok().body(leaderboardService.aroundMe(key, pageSize));
		} catch (Exception e) {
			return notFound().build();
		}
	}
}
