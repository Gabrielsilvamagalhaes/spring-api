package com.gabriel.course.projectapi2;

import com.gabriel.course.projectapi2.dto.PageableDto;
import com.gabriel.course.projectapi2.dto.Vacancies.VacancyResponseDto;
import com.gabriel.course.projectapi2.exceptions.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/vacancies/vacancies-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/vacancies/vacancies-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VacancyIT {

    @Autowired
    WebTestClient testClient;

    //	 Regra para criação de metodos referentes a testes:  motivo do teste_O que será testado_O que sera retornado

    @Test
    public void vacancyCreate_WithAuthorization_ReturnStatus201() {
        testClient.post()
                .uri("/api/vacancies")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PageableDto.VacancyCreateDto("A-50", "FREE"))
                .exchange()
                .expectStatus().isCreated();
    }

    //Tentando inserir um codigo para a vaga ja existente
    @Test
    public void vacancyCreate_VacancyCodeExisting_ReturnStatus409() {
        ErrorMessage responseBody = testClient.post()
                .uri("/api/vacancies")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .bodyValue(new PageableDto.VacancyCreateDto("B-20", "OCUPPIED"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

    }

    //    Cliente tentando realizar cadastro de vaga
    @Test
    public void vacancyCreate_WihtClientInvalidation_ReturnStatus403() {
        ErrorMessage responseBody = testClient.post()
                .uri("/api/vacancies")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                .bodyValue(new PageableDto.VacancyCreateDto("A-22", "FREE"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

    }

    @Test
    public void vacancyCreate_WihtDataInvalidation_ReturnStatus422() {
//        teste para verificar 1 digito a mais no codigo da vaga
        ErrorMessage responseBody = testClient.post()
                .uri("/api/vacancies")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .bodyValue(new PageableDto.VacancyCreateDto("B-500", "FREE"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

//        testes para verificar status da vaga invalido
        responseBody = testClient.post()
                .uri("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .bodyValue(new PageableDto.VacancyCreateDto("B-70", "EXISTIS"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient.post()
                .uri("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .bodyValue(new PageableDto.VacancyCreateDto("", "FREE"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    //    Admin buscando uma vaga por codigo
    @Test
    public void getVacancyByCode_WithCredetialsValid_ReturnStatus200() {
        VacancyResponseDto responseBody = testClient.get()
                .uri("/api/vacancies/B-20")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(VacancyResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getId()).isEqualTo(1000);
        Assertions.assertThat(responseBody.getCode()).isEqualTo("B-20");
        Assertions.assertThat(responseBody.getStatus()).isEqualTo("FREE");
    }

    @Test
    public void getVacancyByCode_WithNotExistingCode_ReturnStatus404() {
        ErrorMessage responseBody = testClient.get()
                .uri("/api/vacancies/B-11")
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
                .uri("/api/vacancies/B-20")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bob@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }
}
