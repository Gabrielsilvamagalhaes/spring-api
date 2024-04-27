package com.gabriel.course.projectapi2.controllers;

import com.gabriel.course.projectapi2.dto.PageableDto;
import com.gabriel.course.projectapi2.dto.ParkingCreateDto;
import com.gabriel.course.projectapi2.dto.ParkingResponseDto;
import com.gabriel.course.projectapi2.dto.mapper.ClientVacancyMapper;
import com.gabriel.course.projectapi2.dto.mapper.PageableMapper;
import com.gabriel.course.projectapi2.exceptions.ErrorMessage;
import com.gabriel.course.projectapi2.jwt.JwtUserDetails;
import com.gabriel.course.projectapi2.repositories.projection.ClientVacancyProjection;
import com.gabriel.course.projectapi2.services.ClientVacancyService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Estacionamentos", description = "Operações de registro de entrada e saída de veículos no estacionamento.")
@RestController
@RequestMapping("api/parkings")
public class ParkingController {
    @Autowired
    ParkingService parkingService;

    @Autowired
    ClientVacancyService clientVacancyService;


    @Operation(summary = "Operação de check-in", description = "Recurso para dar entrada de um veículo no estacionamento." +
            " (Acesso Restrito a admins",
    security = @SecurityRequirement(name = "security"),
    responses = {
            @ApiResponse(responseCode = "201", description = "Check-in realizado com sucesso!",
                    headers = @Header(name = HttpHeaders.LOCATION, description = "URL de acesso no check-in feito."),
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                        schema = @Schema(implementation = ParkingResponseDto.class))
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
    public ResponseEntity<ParkingResponseDto> checkIn(@RequestBody @Valid ParkingCreateDto parking) {
        var clientVacancy = ClientVacancyMapper.toClientVacancy(parking);
        parkingService.checkIn(clientVacancy);
        var responseDto = ClientVacancyMapper.toDto(clientVacancy);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{receipt}")
                .buildAndExpand(clientVacancy.getReceipt())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @Operation(summary = "Operação de busca de check-in", description = "Recurso localiza um check-in através do seu recibo " +
            "(acesso restrito a admins e clientes)",
    security = @SecurityRequirement(name = "security"),
    responses = {
            @ApiResponse(responseCode = "200", description = "check-in localizado com sucesso!",
            content = @Content(mediaType = "application/json;charset=UTF-8",
            schema = @Schema(implementation = ParkingResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "check-in não encontrado ou check-out ja realizado",
            content = @Content(mediaType = "application/json;charset=UTF-8",
            schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/check-in/{receipt}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public  ResponseEntity<ParkingResponseDto> getByReceipt(@PathVariable String receipt) {
        var responseDto = ClientVacancyMapper.toDto(clientVacancyService.findByReceipt(receipt));
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Operação de check-out", description = "Recurso realiza o check-out de um cliente no estacionamento,  através do seu recibo " +
            "(acesso restrito a admins)",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "check-out realizado com sucesso!",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ParkingResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "recibo não encontrado ou veículo ja passado pelo check-out!",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "acesso negado para clientes!",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PutMapping("/check-out/{receipt}")
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<ParkingResponseDto> checkOut(@PathVariable String receipt) {
        var clientVacancy = parkingService.checkOut(receipt);
        return ResponseEntity.ok(ClientVacancyMapper.toDto(clientVacancy));
    }

    @Operation(summary = "Operação de buscar estacionamentos", description = "Recurso realiza a busca de estacionamentos de um cliente,  através do seu cpf " +
            "(acesso restrito a admins)",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "estacionamentos buscados com sucesso!",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ParkingResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "acesso negado para clientes!",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/details/{cpf}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> getParkingsDetails(@PathVariable String cpf,
                                                          @PageableDefault(size = 5, sort = "enterDate", direction = Sort.Direction.ASC
                                                          ) Pageable pageable) {
        Page<ClientVacancyProjection> projection = clientVacancyService.getAllforClientCpf(cpf, pageable);
        var dto = PageableMapper.toDto(projection);
            return  ResponseEntity.ok(dto);
    }

    @Operation(summary = "Operação de buscar estacionamentos", description = "Recurso realiza a busca de estacionamentos do próprio cliente, através do seu id" +
            "(acesso restrito a clientes)",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "estacionamentos do cliente buscados com sucesso!",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ParkingResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "acesso negado para admins!",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/client-details")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<PageableDto> getParkingsDetails(@PageableDefault(size = 5, sort = "enterDate", direction = Sort.Direction.ASC
                                                          ) Pageable pageable, @AuthenticationPrincipal JwtUserDetails user) {
        Page<ClientVacancyProjection> projection = clientVacancyService.getAllForUserId(user.getId(), pageable);
        var dto = PageableMapper.toDto(projection);
            return  ResponseEntity.ok(dto);
    }


}
