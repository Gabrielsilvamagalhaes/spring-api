package com.gabriel.course.projectapi2;

import com.gabriel.course.projectapi2.dto.ParkingCreateDto;
import com.gabriel.course.projectapi2.dto.ParkingReponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

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
        ParkingReponseDto response = testClient.post()
                .uri("/api/parkings/check-in")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ParkingCreateDto("Ford", "Dark Blue", "Fiesta", "LZA-3030", "91468050524"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ParkingReponseDto.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getVacancyCode()).isEqualTo("B-50");
    }

    @Test
    public void createCheckIn_WithInvalidAuthorization_ReturnStatus403() {
        
    }
}
