package com.github.leaderboards;

import java.util.Random;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.leaderboards.rest.LeaderboardService;
import com.github.leaderboards.rest.Member;

@SpringBootApplication
public class LeaderboardsServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaderboardsServicesApplication.class, args);
	}
	
	@Bean
	public InitializingBean init(LeaderboardService service) {
		return () -> {
			init("11053185","http://1.gravatar.com/avatar/8aec855e87715450db1bccac40c48503","Lucas Oliveira","luksrn", service);
			init("95747","http://1.gravatar.com/avatar/a143ada3c03038c998662e914115c016","Larissa Nietto Moura Surano","larissa", service);
			init("1276616","http://0.gravatar.com/avatar/9ea37b2f252951584e00bffed3b2c597","Maiza Moraes","maiza", service);
			init("4587","http://1.gravatar.com/avatar/fa04af3186037e173e81c5eaaf47c1fc","Maria_BB","maria", service);
			init("2163","http://2.gravatar.com/avatar/61831607f42cadf8cf567034b6f1cbe6","Jake Jarvis","jake", service);
			init("274","http://0.gravatar.com/avatar/5102818f67646cdb0b8cd48453b07d20","Jason Hoffman","jason", service);
		};
			
		
	}
		
	private void init(String id, String thumbnail, String name, String displayName, LeaderboardService service) throws JsonProcessingException {
		UserInfo lucas = new UserInfo();
		lucas.setId(id);
		lucas.setThumbnailUrl(thumbnail);
		lucas.setName(name);
		lucas.setDisplayName(displayName);
		
		String jsonLucas = new ObjectMapper().writeValueAsString(lucas);
		
		Member member = new Member();
		
		member.setKey( lucas.getId() );
		member.setScore(  new Random().nextInt(100) );
		member.setMemberData(jsonLucas);
		
		service.rankMember(member);
		
	}
 
}
