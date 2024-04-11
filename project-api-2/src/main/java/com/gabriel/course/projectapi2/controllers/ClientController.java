package com.gabriel.course.projectapi2.controllers;

import com.gabriel.course.projectapi2.dto.ClientCreateDto;
import com.gabriel.course.projectapi2.dto.ClientResponseDto;
import com.gabriel.course.projectapi2.dto.PageableDto;
import com.gabriel.course.projectapi2.dto.UserResponseDto;
import com.gabriel.course.projectapi2.dto.mapper.ClientMapper;
import com.gabriel.course.projectapi2.dto.mapper.PageableMapper;
import com.gabriel.course.projectapi2.exceptions.ErrorMessage;
import com.gabriel.course.projectapi2.jwt.JwtUserDetails;
import com.gabriel.course.projectapi2.model.Client;
import com.gabriel.course.projectapi2.repositories.projection.ClientProjection;
import com.gabriel.course.projectapi2.services.ClientService;
import com.gabriel.course.projectapi2.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "Clientes", description = "Realiza as operações de leitura e adição  de um cliente.")
@RestController
@RequestMapping(value = "/api/clients")
public class ClientController {

    @Autowired
    ClientService clientService;

    @Autowired
    UserService userService;

    @Operation(summary = "Localização de todos os clients", description = "Recurso exige um bearer token, acesso restrito a admin.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Representa a página retornada"
                    ),
                    @Parameter(in = QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5")),
                            description = "Representa o total de elementos por página"
                    ),
                    @Parameter(in = QUERY, name = "sort", hidden = true,
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "nome,asc")),
                            description = "Representa a ordenação dos resultados. Aceita multiplos critérios de ordenação são suportados.")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Clientes resgatados com sucesso!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Não possui permissão para busca!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> getAllClients(Pageable pageable) {
        Page<ClientProjection> clients = clientService.findAllClients(pageable);

        return ResponseEntity.ok(PageableMapper.toDto(clients));
    }


    @Operation(summary = "Localização de um cliente por id", description = "Recurso exige um bearer token, acesso restrito a admin.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente resgatado com sucesso!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente  ão encontrado!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Não possui permissão!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponseDto> getById(@PathVariable Long id) {
        var client = clientService.findById(id);
        return ResponseEntity.ok(ClientMapper.toDto(client));
    }

    @Operation(summary = "Criação de um novo cliente", description = "Recurso usado para criar um cliente, é necessário ser um usuário autenticado",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class ))),
                    @ApiResponse(responseCode = "409", description = "Cpf já cadastrado no sistema!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processo por dados de entrada inválidos!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Admins não pussuem permissão para utilizar do recurso!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDto> createClient(@RequestBody @Valid ClientCreateDto client,
                                                          @AuthenticationPrincipal JwtUserDetails userDetails) {
        var c = ClientMapper.toClient(client);
        c.setUser(userService.findById(userDetails.getId()));
        clientService.saveClient(c);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toDto(c));
    }
}
