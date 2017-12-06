package com.github.leaderboards.web;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.leaderboards.web.resources.MemberRanked;
import com.github.leaderboards.web.resources.Score;

@RestController
public class LeaderboardController {

	@Autowired
	LeaderboardService leaderboardService;
	
	@GetMapping("/rank")
	public ResponseEntity<?> rank(@RequestParam(name="size",defaultValue="20",required=false) Long pageSize){
		try {
			return ok().body(leaderboardService.rank(pageSize));
		} catch (Exception e) {
			return notFound().build();
		}
	}
	
	@PostMapping("/rank")
	public ResponseEntity<?> rank(@RequestBody Score member) {
		try {
			leaderboardService.rankMember(member);
			return ok().build();
		} catch (Exception e) {
			return status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/rank/monthly")
	public ResponseEntity<?> rankMonthly(@RequestParam(name="size",defaultValue="20",required=false) Long pageSize){
		try {
			return ok().body(leaderboardService.rankMonthly(pageSize));
		} catch (Exception e) {
			return notFound().build();
		}
	}
	
	
	@GetMapping("/rank/member/{key}")
	public ResponseEntity<MemberRanked> rank(@PathVariable("key") String key) {
		MemberRanked memberRanked = leaderboardService.rankFor(key);
		if(memberRanked == null) {
			return notFound().build();
		}
		return ok().body(memberRanked);
	}
	
	@GetMapping("/rank/member/{key}/around-me")
	public ResponseEntity<?> aroundMe(@PathVariable("key") String key, 
									@RequestParam(name="size",defaultValue="20",required=false) Long pageSize){
		try {
			return ok().body(leaderboardService.aroundMe(key, pageSize));
		} catch (Exception e) {
			return notFound().build();
		}
	}
	
	@GetMapping("/rank/member/{key}/actions")
	public ResponseEntity<?> actions(@PathVariable("key") String key){
		try {
			return ok().body(leaderboardService.actions(key));
		} catch (Exception e) {
			return notFound().build();
		}
	}
	
	@GetMapping("/rank/member/{key}/scores")
	public ResponseEntity<?> scores(@PathVariable("key") String key){
		try {
			return ok().body(leaderboardService.scores(key));
		} catch (Exception e) {
			e.printStackTrace();
			return notFound().build();
		}
	}
}
