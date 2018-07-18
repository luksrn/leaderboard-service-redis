package com.github.leaderboards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class LeaderboardsServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaderboardsServicesApplication.class, args);
	}
}
