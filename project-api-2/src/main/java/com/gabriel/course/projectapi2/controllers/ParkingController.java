package com.gabriel.course.projectapi2.controllers;

import com.gabriel.course.projectapi2.dto.ParkingCreateDto;
import com.gabriel.course.projectapi2.dto.ParkingReponseDto;
import com.gabriel.course.projectapi2.dto.mapper.ClientVacancyMapper;
import com.gabriel.course.projectapi2.exceptions.ErrorMessage;
import com.gabriel.course.projectapi2.services.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Estacionamentos", description = "Operações de registro de entrada e saída de veículos no estacionamento.")
@RestController
@RequestMapping("api/parkings")
public class ParkingController {
    @Autowired
    ParkingService parkingService;


    @Operation(summary = "Operação de check-in", description = "Recurso para dar entrada de um veículo no estacionamento." +
            " (Acesso Restrito a admins",
    security = @SecurityRequirement(name = "security"),
    responses = {
            @ApiResponse(responseCode = "201", description = "Check-in realizado com sucesso!",
                    headers = @Header(name = HttpHeaders.LOCATION, description = "URL de acesso no check-in feito."),
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                        schema = @Schema(implementation = ParkingReponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Possíveis causas: <br/>" +
                    "- CPF do cliente não está cadadastrado <br/>" +
                    "- Nenhuma vaga livre foi localizada",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                        schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado!",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                        schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Recurso não processo por falta de dados",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                        schema = @Schema(implementation = ErrorMessage.class)))
    })

    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingReponseDto> checkIn(@RequestBody @Valid ParkingCreateDto parking) {
        var clientVacancy = ClientVacancyMapper.toClientVacancy(parking);
        parkingService.checkIn(clientVacancy);
        var responseDto = ClientVacancyMapper.toDto(clientVacancy);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{receipt}")
                .buildAndExpand(clientVacancy.getReceipt())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }
}
