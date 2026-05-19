package com.avengersfc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AvengersFcApplicationTests {

	private static final Pattern ID_PATTERN = Pattern.compile("\"id\"\\s*:\\s*\"([^\"]+)\"");

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void createFeedbackAcceptsJsonGeneratesUuidAndReturnsCreated() throws Exception {
		mockMvc.perform(post("/feedback")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"content\":\"The espresso tastes too bitter.\"}"))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.content").value("The espresso tastes too bitter."));
	}

	@Test
	void createFeedbackRejectsBlankContent() throws Exception {
		mockMvc.perform(post("/feedback")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"content\":\"   \"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void getFeedbackReturnsAllStoredEntries() throws Exception {
		createFeedback("A smooth cappuccino.");

		mockMvc.perform(get("/feedback"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
	}

	@Test
	void updateFeedbackChangesExistingContent() throws Exception {
		String id = createFeedback("Original coffee note.");

		mockMvc.perform(put("/feedback/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"content\":\"Updated coffee note.\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id))
				.andExpect(jsonPath("$.content").value("Updated coffee note."));
	}

	@Test
	void updateFeedbackReturnsNotFoundForInvalidId() throws Exception {
		mockMvc.perform(put("/feedback/{id}", UUID.randomUUID())
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"content\":\"Updated coffee note.\"}"))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteFeedbackRemovesExistingEntry() throws Exception {
		String id = createFeedback("Delete this coffee note.");

		mockMvc.perform(delete("/feedback/{id}", id))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/feedback/{id}", id))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteFeedbackReturnsNotFoundForInvalidId() throws Exception {
		mockMvc.perform(delete("/feedback/{id}", UUID.randomUUID()))
				.andExpect(status().isNotFound());
	}

	private String createFeedback(String content) throws Exception {
		String response = mockMvc.perform(post("/feedback")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"content\":\"" + content + "\"}"))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getContentAsString();

		Matcher matcher = ID_PATTERN.matcher(response);
		if (!matcher.find()) {
			throw new IllegalStateException("Response did not contain a feedback id: " + response);
		}
		return matcher.group(1);
	}
}
