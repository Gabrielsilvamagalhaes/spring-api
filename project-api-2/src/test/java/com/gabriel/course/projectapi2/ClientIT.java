package com.gabriel.course.projectapi2;

import com.gabriel.course.projectapi2.dto.ClientCreateDto;
import com.gabriel.course.projectapi2.dto.ClientResponseDto;
import com.gabriel.course.projectapi2.exceptions.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clients/clients-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clients/clients-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClientIT {

    @Autowired
    WebTestClient testClient;

    //	 Regra para criação de metodos referentes a testes:  motivo do teste_O que será testado_O que sera retornado
    @Test
    public void clientCreate_WitNameAndCpfAndUserValidation_ReturnUserCreatedStatus201() {
        ClientResponseDto responseBody = testClient.post()
                .uri("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "luis@email.com", "123456"))
                .bodyValue(new ClientCreateDto("04933209545", "Luis Soares"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClientResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getId()).isNotNull();
        Assertions.assertThat(responseBody.getCpf()).isEqualTo("04933209545");
        Assertions.assertThat(responseBody.getName()).isEqualTo("Luis Soares");

    }

//    Tentando realizar cadastro com cpf ja existente
    @Test
    public  void clientCreate_WihtCPFExisting_ReturnStatus409() {
        ErrorMessage responseBody = testClient.post()
                .uri("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "luis@email.com", "123456"))
                .bodyValue(new ClientCreateDto("05673774583", "Luis Soares"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

    }

//    Admin tentando realizar cadastro de cliente
    @Test
    public  void clientCreate_WihtAdminInvalidation_ReturnStatus403() {
        ErrorMessage responseBody = testClient.post()
                .uri("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .bodyValue(new ClientCreateDto("63441624572", "Ana Soares"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

    }

    @Test
    public  void clientCreate_WihtDataInvalidation_ReturnStatus422() {
//        teste para verificar 1 digito a menos no cpf
        ErrorMessage responseBody = testClient.post()
                .uri("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "luis@email.com", "123456"))
                .bodyValue(new ClientCreateDto("6344162457", "Luis Soares"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

//        testes para verificar nome invalido
        responseBody = testClient.post()
                .uri("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "luis@email.com", "123456"))
                .bodyValue(new ClientCreateDto("6344162457", "Lu"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient.post()
                .uri("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "luis@email.com", "123456"))
                .bodyValue(new ClientCreateDto("6344162457", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @Test
    public void getClientById_WithCredetialsValid_ReturnStatus200() {
        ClientResponseDto responseBody = testClient.get()
                .uri("/api/clients/11")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClientResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getId()).isEqualTo(11);
        Assertions.assertThat(responseBody.getName()).isEqualTo("Bianca Lacerda");
        Assertions.assertThat(responseBody.getCpf()).isEqualTo("05673774583");
    }

    @Test
    public void getClientById_WithNotExistingId_ReturnStatus404() {
        ErrorMessage responseBody = testClient.get()
                .uri("/api/clients/3")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);

    }

    @Test
    public void getClientById_WithNotAuthorization_ReturnStatus403() {
        ErrorMessage responseBody = testClient.get()
                .uri("/api/clients/11")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bob@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }


}
