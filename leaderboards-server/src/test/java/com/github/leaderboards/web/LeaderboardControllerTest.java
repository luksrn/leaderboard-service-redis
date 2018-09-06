package com.github.leaderboards.web;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class LeaderboardControllerTest {

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private WebApplicationContext context;
	
	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(this.restDocumentation)) 
				.build();
	}
	
	@Test
	public void exampleTest() throws Exception {
		this.mvc.perform(get("/rank")
				.accept("application/hal+json"))
				.andExpect(status().isOk())
				.andDo(document("rank", 
						responseFields(
								fieldWithPath("_embedded").ignored(),
								fieldWithPath("_embedded.membersRanked").description("List of top 20 members ranked"),
								fieldWithPath("_embedded.membersRanked[].key").description("KEy"),
								fieldWithPath("_embedded.membersRanked[].rank").description("KEy"),
								fieldWithPath("_embedded.membersRanked[].score").description("KEy"),
								fieldWithPath("_embedded.membersRanked[].userData.displayName").description(""),
								fieldWithPath("_embedded.membersRanked[].userData.name").description(""),
								fieldWithPath("_embedded.membersRanked[].userData.id").description("id"),
								fieldWithPath("_embedded.membersRanked[].userData.thumbnailUrl").description("")
						)));
		
		/**
		 * ,
						links(
								linkWithRel("_self").ignored(),
								linkWithRel("arround-me").description(""),
								linkWithRel("scores").description(""),
								linkWithRel("latest-activities").description("")
						)
		 */
	}
	

	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
}
