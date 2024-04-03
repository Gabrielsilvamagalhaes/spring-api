package com.gabriel.course.projectapi2;

import com.gabriel.course.projectapi2.dto.UserPassDto;
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

import java.util.List;

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

	@Test
	public void userGet_WithGetById_ReturnUserStatus200() {
//		Teste para admin buscando ele proóprio por id
		UserResponseDto responseBody = testClient.get()
				.uri("/api/users/100")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
				.exchange()
				.expectStatus().isOk()
				.expectBody(UserResponseDto.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getId()).isEqualTo(100);
		Assertions.assertThat(responseBody.getUsername()).isEqualTo("ana@email.com");
		Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");

//		Teste para admin buscando cliente por id
		responseBody = testClient.get()
				.uri("/api/users/101")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
				.exchange()
				.expectStatus().isOk()
				.expectBody(UserResponseDto.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getId()).isEqualTo(101);
		Assertions.assertThat(responseBody.getUsername()).isEqualTo("bia@email.com");
		Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENT");

//		Teste para cliente buscando ele proóprio por id
		responseBody = testClient.get()
				.uri("/api/users/101")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
				.exchange()
				.expectStatus().isOk()
				.expectBody(UserResponseDto.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getId()).isEqualTo(101);
		Assertions.assertThat(responseBody.getUsername()).isEqualTo("bia@email.com");
		Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENT");

	}

	@Test
	public void userGet_WithInvalidGetById_ReturnStatus404() {
//		Admin buscando usuário por id inexistente
		ErrorMessage responseBody = testClient.get()
				.uri("/api/users/1")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
				.exchange()
				.expectStatus().isEqualTo(404)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);


	}

	@Test
	public void userGet_WithInvalidGetById_ReturnStatus403() {
//		Cliente buscando outro id sem ser o dele(cliente nao tem permissao para isso)
		ErrorMessage responseBody = testClient.get()
				.uri("/api/users/100")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
				.exchange()
				.expectStatus().isEqualTo(403)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);


	}

	@Test
	public void userPut_WithPassValidation_ReturnUserUpdatedStatus204() {
//Admin modificando sua senha
		testClient.put()
				.uri("/api/users/100")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserPassDto("123456", "654321", "654321"))
				.exchange()
				.expectStatus().isNoContent();

//Cliente modificando sua senha
		testClient.put()
				.uri("/api/users/101")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserPassDto("123456", "654321", "654321"))
				.exchange()
				.expectStatus().isNoContent();
	}

	@Test
	public void userPut_WithInvalidAuthentication_ReturnStatus403() {
//		Cliente tentando alterar senha de outro usuário
		ErrorMessage responseBody = testClient.put()
				.uri("/api/users/100")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserPassDto("123456", "654322", "654321"))
				.exchange()
				.expectStatus().isEqualTo(403)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

//		Admin tentando alterar senha de outro usuário
		responseBody = testClient.put()
				.uri("/api/users/101")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserPassDto("123456", "654322", "654321"))
				.exchange()
				.expectStatus().isEqualTo(403)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

	}

	@Test
	public void userPut_WithInvalidPass_ReturnStatus400() {
//		Usuario inserindo senha de confirmação errada
		ErrorMessage responseBody = testClient.put()
				.uri("/api/users/100")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserPassDto("123456", "654322", "654321"))
				.exchange()
				.expectStatus().isEqualTo(400)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

//		Usuario inserindo senha atual errada
		responseBody = testClient.put()
				.uri("/api/users/100")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UserPassDto("123455", "654321", "654321"))
				.exchange()
				.expectStatus().isEqualTo(400)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);


	}

	@Test
	public void usersGet_WithGetAll_ReturnUserStatus200() {
//		Admin buscando todos os usuários
		List<UserResponseDto> responseBody = testClient.get()
				.uri("/api/users")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(UserResponseDto.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.size()).isEqualTo(3);

	}

	@Test
	public void usersGet_WithInvalidGetAll_ReturnUsersStatus403() {
//		Cliente tentando buscar todos os usuários
		ErrorMessage responseBody = testClient.get()
				.uri("/api/users")
				.headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
				.exchange()
				.expectStatus().isEqualTo(403)
				.expectBody(ErrorMessage.class)
				.returnResult().getResponseBody();

		Assertions.assertThat(responseBody).isNotNull();
		Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);


	}
}
