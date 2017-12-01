package com.github.leaderboards.rest;

public class MemberRanked {

	private String key;
	
	private Long rank;
	
	private double score;
	
	private String userData;
	
	public MemberRanked() {
	}
	
	public MemberRanked(String key,double score, Long rank) {
		this.key = key;
		this.rank = rank;
		this.score = score;
	}
	
	public MemberRanked(String key,double score, Long rank, String userData) {
		this.key = key;
		this.rank = rank;
		this.score = score;
		this.userData = userData;
	}
	
	public void setUserData(String userData) {
		this.userData = userData;
	}
	
	public String getUserData() {
		return userData;
	}
	
	public String getKey() {
		return key;
	}

	public Long getRank() {
		return rank;
	}
	
	public double getScore() {
		return score;
	}
}
