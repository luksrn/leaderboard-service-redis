package com.github.leaderboards.web.resources;

import java.time.LocalDate;

public class ScoreDay {

	private LocalDate day;
	
	private double scores;

	public ScoreDay(LocalDate d) {
		day = d;
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public double getScores() {
		return scores;
	}

	public void setScores(double scores) {
		this.scores = scores;
	}
}
