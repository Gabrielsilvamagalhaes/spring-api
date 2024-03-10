package com.gabriel.course.projectapi2;

import com.gabriel.course.projectapi2.exceptions.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.gabriel.course.projectapi2.dto.UserCreateDto;
import com.gabriel.course.projectapi2.dto.UserResponseDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class UserIT {

	@Autowired
	WebTestClient testClient;
	
//	O motivo do teste_O que será testado_O que sera retornado
	@Test
	public void userCreate_WithUsernameAndPassValidation_ReturnUserCreatedStatus201() {
		UserResponseDto responseBody = testClient.post()
				.uri("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserCreateDto("gabiles278@email.com", "123456"))
				.exchange()
				.expectStatus().isCreated()
				.expectBody(UserResponseDto.class)
				.returnResult().getResponseBody();
		
		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getId()).isNotNull();
		Assertions.assertThat(responseBody.getUsername()).isEqualTo("gabiles278@email.com");
		Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENT");
		
	}
	@Test
	public void userCreate_WithInvalidUsername_ReturnStatus422() {
		ErrorMessage responseBody = testClient.post()
				.uri("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserCreateDto("", "123456"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

		responseBody = testClient.post()
				.uri("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserCreateDto("tod@", "123456"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

		responseBody = testClient.post()
				.uri("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserCreateDto("tod@email", "123456"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

	}
	@Test
	public void userCreate_WithInvalidPass_ReturnStatus422() {
		ErrorMessage responseBody = testClient.post()
				.uri("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserCreateDto("gabiles278@email.com", ""))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

		responseBody = testClient.post()
				.uri("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserCreateDto("gabiles278@email.com", "1234566"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

		responseBody = testClient.post()
				.uri("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserCreateDto("gabiles278@email.com", "1"))
				.exchange()
				.expectStatus().isEqualTo(422)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

	}

	@Test
	public void userCreate_WithUsernameExists_ReturnStatus409() {
		ErrorMessage responseBody = testClient.post()
				.uri("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserCreateDto("arroz@gmail.com", "123456"))
				.exchange()
				.expectStatus().isEqualTo(409)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);


	}
}
