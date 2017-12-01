package com.github.leaderboards.rest;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;



public class Member {

	/**
	 *  Member Ids
	 */
	@NotBlank
	private String key;
	
	@Min(value=0)
	private double score;
	
	/**
	 * Describes the reason for the score.
	 */
	private String description;
	
	/**
	 * User informations.
	 */
	private String memberData;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getMemberData() {
		return memberData;
	}

	public void setMemberData(String memberData) {
		this.memberData = memberData;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
