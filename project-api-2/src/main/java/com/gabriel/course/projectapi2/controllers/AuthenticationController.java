package com.gabriel.course.projectapi2.controllers;

import com.gabriel.course.projectapi2.dto.Users.UserLoginDto;
import com.gabriel.course.projectapi2.dto.Users.UserResponseDto;
import com.gabriel.course.projectapi2.exceptions.ErrorMessage;
import com.gabriel.course.projectapi2.jwt.JwtToken;
import com.gabriel.course.projectapi2.jwt.JwtUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Recurso para autenticação na api.")
@RequestMapping("api/auth")
@RestController
@Slf4j
public class AuthenticationController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUserDetailsService detailsService;

    @Operation(summary = "Autenticão de usuário", description = "Recurso autentica o usuário para utilizar recursos que necessitam de autenticação. Retornando, um beare token.",

            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "422", description = "Usuário não possui login ou campos de acesso inválido!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "400", description = "Credenciais Invalidas!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<?> authenticate (@RequestBody @Valid  UserLoginDto dto, HttpServletRequest request) {
        log.info("Processo de autenticacao pelo login {}", dto.getUsername());

        try{
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
            authenticationManager.authenticate(authenticationToken);

            JwtToken token = detailsService.getToken(dto.getUsername());
            return  ResponseEntity.ok(token);

        }catch (AuthenticationException exception) {
            log.warn("Erro na autenticação");
        }

        return  ResponseEntity.badRequest().body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Credenciais Invalidas!"));
    }

}