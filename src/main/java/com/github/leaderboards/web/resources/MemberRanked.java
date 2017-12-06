package com.github.leaderboards.web.resources;

import java.util.HashMap;

import org.springframework.hateoas.ResourceSupport;

public class MemberRanked extends ResourceSupport {

	private String key;
	
	private Long rank;
	
	private double score;
	
	private HashMap<String,?> userData;
	
	public MemberRanked() {
	}
	
	public MemberRanked(String key,double score, Long rank) {
		this.key = key;
		this.rank = rank;
		this.score = score;
	}
	
	public MemberRanked(String key,double score, Long rank, HashMap<String,?> userData) {
		this.key = key;
		this.rank = rank;
		this.score = score;
		this.userData = userData;
	}
	
	public void setUserData(HashMap<String,?> userData) {
		this.userData = userData;
	}
	
	public HashMap<String,?> getUserData() {
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
