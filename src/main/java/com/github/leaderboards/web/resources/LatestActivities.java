package com.github.leaderboards.web.resources;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LatestActivities {

	@JsonIgnore
	private String key;
	
	private List<Activity> activities = new ArrayList<>();
	
	public LatestActivities(String key) {
		super();
		this.key = key;
	}
	
	public LatestActivities(String key,List<Activity> activities) {
		super();
		this.key = key;
		this.activities = activities;
	}
	
	public LatestActivities add(Activity a) {
		activities.add(a); 
		return this;
	}

	public List<Activity> getActivities() {
		return activities;
	}
	
	public String getKey() {
		return key;
	}
}
