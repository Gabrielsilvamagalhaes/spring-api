package com.gabriel.course.projectapi2;

import com.gabriel.course.projectapi2.dto.PageableDto;
import com.gabriel.course.projectapi2.dto.ParkingCreateDto;
import com.gabriel.course.projectapi2.dto.ParkingResponseDto;
import com.gabriel.course.projectapi2.exceptions.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/parkings/parking-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/parkings/parking-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ParkingIT {

    @Autowired
    WebTestClient testClient;

    //	 Regra para criação de metodos referentes a testes:  motivo do teste_O que será testado_O que sera retornado

    @Test
    public void createCheckIn_WithDataValidation_ReturnStatus201() {
        ParkingResponseDto response = testClient.post()
                .uri("/api/parkings/check-in")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ParkingCreateDto("Ford", "Dark Blue", "Fiesta", "LZA-3030", "91468050524"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ParkingResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getVacancyCode()).isEqualTo("B-50");
    }

    @Test
    public void createCheckIn_WithInvalidAuthorization_ReturnStatus403() {
//        Usuário tentando realizar o checkIn de uma vaga
        ErrorMessage response = testClient.post()
                .uri("/api/parkings/check-in")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ParkingCreateDto("Ford", "Dark Blue", "Fiesta", "LZA-3030", "91468050524"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void createCheckIn_WithDataInvalidation_ReturnStatus422() {
//        Inserindo valores inválidos ou nulos
        ErrorMessage response = testClient.post()
                .uri("/api/parkings/check-in")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ParkingCreateDto("Ford", "Dark Blue", "Fiesta", "LZA-303", "91468050524"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(422);

        response = testClient.post()
                .uri("/api/parkings/check-in")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ParkingCreateDto("Ford", "Dark Blue", "Fiesta", "LZ3-303A", "91468050524"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(422);

        response = testClient.post()
                .uri("/api/parkings/check-in")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ParkingCreateDto("Ford", " ", " ", "LZA-3030", "91468050524"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    public void createCheckIn_WithCpfNotExists_ReturnStatus404() {
//        Tentando realizar um CheckIn com o cpf inexistente na base de dados
        ErrorMessage response = testClient.post()
                .uri("/api/parkings/check-in")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ParkingCreateDto("Ford", "Red", "Fiat", "LZA-3030", "15721016019"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Sql(scripts = "/sql/parkings/parking-insert-vacancys-ocuppieds.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/parkings/parking-delete-vacancys-ocuppieds.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void createCheckIn_WithVacancysOcuppieds_ReturnStatus404() {
        ErrorMessage response = testClient.post()
                .uri("/api/parkings/check-in")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ParkingCreateDto("Ford", "Dark Blue", "Fiesta", "LZA-3030", "91468050524"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    public  void getCheckInData_WithReceiptValidation_ReturnStatus200() {
//        Admin buscando os dados de check-in de um estacionamento realizado por um cliente
        ParkingResponseDto response = testClient.get()
                .uri("api/parkings/check-in/20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ParkingResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getReceipt()).isEqualTo("20230313-101300");

//        Cliente buscando os dados do seu check-in de um estacionamento
        response = testClient.get()
                .uri("api/parkings/check-in/20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ParkingResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getReceipt()).isEqualTo("20230313-101300");
    }

    @Test
    public void  getCheckInData_WithReceiptInvalidation_ReturnStatus404() {
//        Admin buscando os dados de um check-in com o recibo inválido
        ErrorMessage response = testClient.get()
                .uri("api/parkings/check-in/20230313-103245")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    public  void createCheckOut_WiThDataValidation_ReturnStatus200() {
        ParkingResponseDto response = testClient.put()
                .uri("api/parkings/check-out/20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ParkingResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getReceipt()).isEqualTo("20230313-101300");

    }

    @Sql(scripts = "/sql/parkings/parking-insert-vacancys-check-out.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/parkings/parking-delete-vacancys-check-out.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void createCheckOut_WithDataInvalidation_ReturnStatus404 () {
//        admin realizando o check-out com um recibo inválido
        ErrorMessage response = testClient.put()
                .uri("api/parkings/check-out/20230313-101322")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);

//        admin realizando o check-out de um recibo que ja possui check-out

        response = testClient.put()
                .uri("api/parkings/check-out/20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    public void createCheckOut_WithAuthenticationInvalidation_ReturnStatus403() {
//        Cliente tentando realizar um check-out
        ErrorMessage response = testClient.put()
                .uri("api/parkings/check-out/20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void getParkingDetails_WithCpfValidation_ReturnStatus200() {
        PageableDto response = testClient.get()
                .uri("api/parkings/details/05673774583")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto .class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getTotalElements()).isEqualTo(2);
        assertThat(response.getTotalPages()).isEqualTo(1);
        assertThat(response.getNumberOfElements()).isEqualTo(2);

    }

    @Test
    public void getParkingDetails_WithAuthenticationInvalidation_ReturnStatus403() {
        ErrorMessage response = testClient.get()
                .uri("api/parkings/details/05673774583")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void getParkingClientDetails_WithAuthenticationValidation_ReturnStatus200() {
        PageableDto response = testClient.get()
                .uri("api/parkings/client-details")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getTotalElements()).isEqualTo(2);
        assertThat(response.getTotalPages()).isEqualTo(1);
        assertThat(response.getNumberOfElements()).isEqualTo(2);

    }

    @Test
    public void getParkingClientDetails_WithAuthenticationInvalidation_ReturnStatus403() {
        ErrorMessage response = testClient.get()
                .uri("api/parkings/details/05673774583")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(403);
    }

}
