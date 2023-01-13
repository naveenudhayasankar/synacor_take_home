package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(QuotaController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuotaControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	@Order(1)
	public void testGet() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/consume"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.total").value(0))
				.andExpect(MockMvcResultMatchers.jsonPath("$.remaining").value(1000));
	}

	@Test
	@Order(2)
	public void testConsume() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/consume").content(asJsonString(new PostRequest(200)))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.total").value(200))
				.andExpect(MockMvcResultMatchers.jsonPath("$.remaining").value(800));
	}

	@Test
	@Order(3)
	public void testExcessConsume() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/consume").content(asJsonString(new PostRequest(1000)))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is5xxServerError())
				.andExpect(MockMvcResultMatchers.jsonPath("$.excess").value(200));
	}

	@Test
	@Order(4)
	public void testReset() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/reset").content(asJsonString(new ResetRequest(2000)))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.total").value(0))
				.andExpect(MockMvcResultMatchers.jsonPath("$.remaining").value(2000));
	}

	@Test
	@Order(5)
	public void testGetAfterReset() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/consume"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.total").value(0))
				.andExpect(MockMvcResultMatchers.jsonPath("$.remaining").value(2000));
	}

	@Test
	@Order(6)
	public void testConsumeAfterReset() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/consume").content(asJsonString(new PostRequest(200)))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.total").value(200))
				.andExpect(MockMvcResultMatchers.jsonPath("$.remaining").value(1800));
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}


