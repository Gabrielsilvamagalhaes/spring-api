package com.gabriel.course.projectapi2.controllers;

import com.gabriel.course.projectapi2.dto.UserResponseDto;
import com.gabriel.course.projectapi2.dto.VacancyCreateDto;
import com.gabriel.course.projectapi2.dto.VacancyResponseDto;
import com.gabriel.course.projectapi2.dto.mapper.VacancyMapper;
import com.gabriel.course.projectapi2.exceptions.ErrorMessage;
import com.gabriel.course.projectapi2.services.VacancyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/vacancies")
public class VacancyController {

    @Autowired
    VacancyService vacancyService;


    @Operation(summary = "Localização de uma vaga por código", description = "Recurso exige um bearer token, acesso restrito a admin.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vaga resgatada com sucesso!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Vaga  não encontrada!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Não possui permissão!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VacancyResponseDto> getByCode(@PathVariable String code) {
        var vacancy = vacancyService.findByCode(code);
        return ResponseEntity.ok(VacancyMapper.toDto(vacancy));
    }


    @Operation(summary = "Criação de uma nova vaga", description = "Recurso usado para criar uma vaga, é necessário ser um admin autenticado",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Vaga criada com sucesso!",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL do recurso criado")),
                    @ApiResponse(responseCode = "409", description = "Código de vaga já cadastrado no sistema!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processo por dados de entrada inválidos!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Usuários não pussuem permissão para utilizar do recurso!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid VacancyCreateDto createDto) {
         vacancyService.save(VacancyMapper.toVacancy(createDto));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{code}")
                .buildAndExpand(createDto.getCode())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
