package com.gabriel.course.projectapi2.controllers;

import com.gabriel.course.projectapi2.dto.ClientCreateDto;
import com.gabriel.course.projectapi2.dto.ClientResponseDto;
import com.gabriel.course.projectapi2.dto.mapper.ClientMapper;
import com.gabriel.course.projectapi2.jwt.JwtUserDetails;
import com.gabriel.course.projectapi2.model.Client;
import com.gabriel.course.projectapi2.services.ClientService;
import com.gabriel.course.projectapi2.services.UserService;
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

@RestController
@RequestMapping(value = "/api/clients")
public class ClientController {

    @Autowired
    ClientService clientService;

    @Autowired
    UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Client>> getAllClients(Pageable pageable) {
        Page<Client> clients = clientService.findAllClients(pageable);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponseDto> getById(@PathVariable Long id) {
        var client = clientService.findById(id);
        return ResponseEntity.ok(ClientMapper.toDto(client));
    }

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
