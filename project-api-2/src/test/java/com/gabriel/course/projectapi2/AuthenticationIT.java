package com.gabriel.course.projectapi2;

import com.gabriel.course.projectapi2.dto.Users.UserLoginDto;
import com.gabriel.course.projectapi2.exceptions.ErrorMessage;
import com.gabriel.course.projectapi2.jwt.JwtToken;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthenticationIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void authPost_WIthValidCrendentials_ReturnStatus200 () {
//        Usuário realizando autenticação
            JwtToken responseBody = testClient.post()
                    .uri("/api/auth")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UserLoginDto("ana@email.com", "123456"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(JwtToken.class)
                    .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();

    }

    @Test
    public void authPost_WithWrongCredentials_ReturnStatus400(){
//        Usuário inserindo credencial errada para a autenticação
        ErrorMessage responseBody = testClient.post()
                .uri("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserLoginDto("ana@email.com", "123457"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = testClient.post()
                .uri("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserLoginDto("ana@email", "123456"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);


    }

    @Test
    public void authPost_WithInvalidCredentials_Return422() {
//        Usuário inserindo credenciais inválidas na autenticação
        ErrorMessage responseBody = testClient.post()
                .uri("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserLoginDto("ana@email.com", "12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);




    }

}
