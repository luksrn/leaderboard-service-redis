package com.github.leaderboards.rabbitmq;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.leaderboards.web.LeaderboardService;
import com.github.leaderboards.web.resources.Score;

@Component
public class LeaderboardListener {

	@Autowired
	private LeaderboardService leaderboardService;
	
	@RabbitListener(bindings= {
			@QueueBinding(
					value=@Queue(name="rank"),
					exchange=@Exchange(name="leaderboard")
			)
	})
	public void process(Score memberScore) {
		leaderboardService.rankMember(memberScore);
	}
}
