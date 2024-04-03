package com.gabriel.course.projectapi2;

import com.gabriel.course.projectapi2.dto.UserLoginDto;
import com.gabriel.course.projectapi2.jwt.JwtToken;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

public class JwtAuthentication {

    public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient testClient, String username, String password) {
        String token = testClient.post()
                .uri("/api/auth")
                .bodyValue(new UserLoginDto(username, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody().getToken();

        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

    }
}
