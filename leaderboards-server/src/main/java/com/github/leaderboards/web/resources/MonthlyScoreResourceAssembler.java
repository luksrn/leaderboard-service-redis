package com.github.leaderboards.web.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.github.leaderboards.web.LeaderboardController;
import com.github.leaderboards.web.resources.MonthlyScoreResourceAssembler.MonthlyScoreResource;

@Component
public class MonthlyScoreResourceAssembler extends ResourceAssemblerSupport<MonthlyScore, MonthlyScoreResource> {

	public MonthlyScoreResourceAssembler() {
		super(LeaderboardController.class,MonthlyScoreResource.class);
	}
	
	@Override
	public MonthlyScoreResource toResource(MonthlyScore entity) {
		MonthlyScoreResource resource = new MonthlyScoreResource(entity);

		resource.add( linkTo(methodOn(LeaderboardController.class).scores(entity.getKey())).withSelfRel());
		resource.add( linkTo(methodOn(LeaderboardController.class).rank(entity.getKey())).withRel("rank") );
		resource.add( linkTo(methodOn(LeaderboardController.class).aroundMe(entity.getKey(), null)).withRel("arround-me"));
		resource.add( linkTo(methodOn(LeaderboardController.class).actions(entity.getKey())).withRel("latest-activities"));
		return resource;
	}
	
	public static class MonthlyScoreResource extends Resource<MonthlyScore>{
		
		public MonthlyScoreResource(MonthlyScore entity) {
			super(entity);
		}
	}
}
