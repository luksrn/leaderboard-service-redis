package com.github.leaderboards.web.resources;

import java.util.HashMap;

import org.springframework.hateoas.core.Relation;



@Relation(value = "memberRanked",collectionRelation="membersRanked")
public class MemberRanked  {

	private String key;
	
	private Long rank;
	
	private Double score;
	
	private HashMap<String,?> userData;
	
	public MemberRanked() {
	}
	
	public MemberRanked(String key) {
		this.key = key;
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
	
	public Double getScore() {
		return score;
	}
}
