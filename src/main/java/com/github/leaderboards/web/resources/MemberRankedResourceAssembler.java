package com.github.leaderboards.web.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.github.leaderboards.web.LeaderboardController;
import com.github.leaderboards.web.resources.MemberRankedResourceAssembler.MemberRankedResource;

@Component
public class MemberRankedResourceAssembler extends ResourceAssemblerSupport<MemberRanked, MemberRankedResource> {

	public MemberRankedResourceAssembler() {
		super(LeaderboardController.class, MemberRankedResource.class);
	}

	@Override
	public MemberRankedResource toResource(MemberRanked entity) {
		MemberRankedResource resource = new MemberRankedResource(entity);
		
		resource.add( linkTo(methodOn(LeaderboardController.class).rank(entity.getKey())).withSelfRel());
		resource.add( linkTo(methodOn(LeaderboardController.class).aroundMe(entity.getKey(), null)).withRel("arround-me"));
		resource.add( linkTo(methodOn(LeaderboardController.class).scores(entity.getKey())).withRel("scores"));
		resource.add( linkTo(methodOn(LeaderboardController.class).actions(entity.getKey())).withRel("latest-activities"));
		
		return resource;
	}
	
	public static class MemberRankedResource extends Resource<MemberRanked>{

		public MemberRankedResource(MemberRanked content) {
			super(content);
		}
		
	}
}
