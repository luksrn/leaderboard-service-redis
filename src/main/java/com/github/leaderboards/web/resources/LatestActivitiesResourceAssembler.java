package com.github.leaderboards.web.resources;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.github.leaderboards.web.LeaderboardController;
import com.github.leaderboards.web.resources.LatestActivitiesResourceAssembler.LatestActivitiesResource;

@Component
public class LatestActivitiesResourceAssembler extends ResourceAssemblerSupport<LatestActivities, LatestActivitiesResource>{
	
	public LatestActivitiesResourceAssembler() {
		super(LeaderboardController.class, LatestActivitiesResource.class);
	}

	static class LatestActivitiesResource extends Resource<LatestActivities>{
		
		public LatestActivitiesResource(LatestActivities entity) {
			super(entity);
		}
	}

	@Override
	public LatestActivitiesResource toResource(LatestActivities entity) {
		LatestActivitiesResource resource = new LatestActivitiesResource(entity);
		resource.add( linkTo(methodOn(LeaderboardController.class).actions(entity.getKey())).withSelfRel() );
		resource.add( linkTo(methodOn(LeaderboardController.class).scores(entity.getKey())).withRel("scores"));
		resource.add( linkTo(methodOn(LeaderboardController.class).rank(entity.getKey())).withRel("rank"));
		return resource;
	}
}
