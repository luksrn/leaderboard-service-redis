package com.github.leaderboards.leaderboardseurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class LeaderboardsEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaderboardsEurekaServerApplication.class, args);
	}
}
