package com.github.leaderboards.web.resources;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.databind.JsonNode;


public class Score {

	/**
	 *  Member Ids
	 */
	@NotBlank
	private String userId;
	
	@NotNull
	private Double score;
	
	/**
	 * the reason why the user gain theses points
	 */
	@NotNull
	private String description;

	/**
	 * Momento em que a pontuação ocorreu.
	 */
	@JsonIgnore
	private LocalDateTime moment = LocalDateTime.now();
	
	private UserInfo memberData;

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

	public UserInfo getMemberData() {
		return memberData;
	}

	public void setMemberData(UserInfo memberData) {
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
