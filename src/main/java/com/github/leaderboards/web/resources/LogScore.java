package com.github.leaderboards.web.resources;

import java.time.LocalDateTime;

public class LogScore {

	private double score;
	
	private String description;
	
	private LocalDateTime timestamp;
	
	public LogScore() {
		super();
	}

	public LogScore(double score, String description, LocalDateTime timestamp) {
		this.score = score;
		this.description = description;
		this.timestamp = timestamp;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
