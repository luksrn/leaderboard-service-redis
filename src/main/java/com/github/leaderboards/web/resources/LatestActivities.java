package com.github.leaderboards.web.resources;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LatestActivities {

	@JsonIgnore
	private String key;
	
	private List<Activity> activities;
	
	public LatestActivities(String key,List<Activity> activities) {
		super();
		this.key = key;
		this.activities = activities;
	}

	public List<Activity> getActivities() {
		return activities;
	}
	
	public String getKey() {
		return key;
	}
}
