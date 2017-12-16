package com.github.leaderboards.web.resources;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MonthlyScore {

	@JsonIgnore
	private String key;
	
	private List<ScoreDay> scores;

	public MonthlyScore(String key, List<ScoreDay> scores) {
		super();
		this.key = key;
		this.scores = scores;
	}
	
	public String getKey() {
		return key;
	}
	
	public List<ScoreDay> getScores() {
		return scores;
	}
}
