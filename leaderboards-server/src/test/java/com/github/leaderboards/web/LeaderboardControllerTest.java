package com.github.leaderboards.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.leaderboards.web.resources.Score;
import com.github.leaderboards.web.resources.UserInfo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.hypermedia.LinkDescriptor;
import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class LeaderboardControllerTest {

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
	
	private MockMvc mvc;

	@Autowired
	private WebApplicationContext context;
	
	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation)
					.operationPreprocessors()
					.withResponseDefaults(Preprocessors.prettyPrint())
					.withRequestDefaults(Preprocessors.prettyPrint()))
				.build();

	}


	@Test
	public void createMemberRanked() throws Exception{

		Score score = new Score();
		score.setUserId("10");
		score.setDescription("Spring framework - Module 01 complete!");
		score.setMoment(null);
		score.setScore(250d);
		UserInfo user = new UserInfo();
		user.setId(10L);
		user.setName("luksrn");
		user.setDisplayName("Lucas Farias de Oliveira");
		user.setThumbnailUrl("https://secure.gravatar.com/avatar/8aec855e87715450db1bccac40c48503");
		score.setMemberData(user);

		this.mvc.perform(MockMvcRequestBuilders.post("/rank")
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(score))
					.characterEncoding("UTF-8"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andDo(MockMvcRestDocumentation.document("create-rank"))
				.andReturn();
	}
	
	@Test
	public void viewCreatedMemberRanked() throws Exception {
		this.mvc.perform(get("/rank/member/{userKey}",10)
				.accept("application/hal+json")
				.characterEncoding("UTF-8"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andDo(document("rank-member-view", responseFields(user),
						HypermediaDocumentation.links(HypermediaDocumentation.halLinks(),usersRankedHatoasLinks)));

	}

	private LinkDescriptor[] usersRankedHatoasLinks = new LinkDescriptor [] {
					HypermediaDocumentation.linkWithRel("self").description("teste"),
					HypermediaDocumentation.linkWithRel("arround-me").description(""),
					HypermediaDocumentation.linkWithRel("scores").description(""),
					HypermediaDocumentation.linkWithRel("latest-activities").description("") };


	private FieldDescriptor[] user=  new FieldDescriptor[]{
			fieldWithPath("key").description("KEy"),
			fieldWithPath("rank").description("KEy"),
			fieldWithPath("score").description("KEy"),
			fieldWithPath("userData.displayName").description(""),
			fieldWithPath("userData.name").description(""),
			fieldWithPath("userData.id").description("id"),
			fieldWithPath("userData.thumbnailUrl").description(""),
			subsectionWithPath("_links").description("teste")
	} ;

	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
}
