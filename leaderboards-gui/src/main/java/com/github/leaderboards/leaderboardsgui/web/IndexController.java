package com.github.leaderboards.leaderboardsgui.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class IndexController {

	@Autowired
	RestTemplate restTemplate;
	
	@RequestMapping("/")
	public String index(Model model) {
		
		model.addAttribute("rankJson", restTemplate.getForEntity("http://leaderboards-service/rank", String.class) );
		return "index";
	}
}
