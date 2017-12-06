package com.github.leaderboards.web.resources;

import java.time.LocalDateTime;

import javax.validation.constraints.Null;

import org.hibernate.validator.constraints.NotBlank;


public class Score {

	/**
	 *  Member Ids
	 */
	@NotBlank
	private String userId;
	
	private double score;
	
	/**
	 * the reason why the user gain theses points
	 */
	private String description;

	/**
	 * Momento em que a pontuação ocorreu.
	 */
	@Null
	private LocalDateTime moment = LocalDateTime.now();
	
	/**
	 * User informations as JSON.
	 */
	private String memberData;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String key) {
		this.userId = key;
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
	
	public LocalDateTime getMoment() {
		return moment;
	}
	
	public void setMoment(LocalDateTime moment) {
		this.moment = moment;
	}

}
