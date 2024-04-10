package com.gabriel.course.projectapi2.controllers;

import com.gabriel.course.projectapi2.dto.ClientCreatDto;
import com.gabriel.course.projectapi2.dto.ClientResponseDto;
import com.gabriel.course.projectapi2.dto.mapper.ClientMapper;
import com.gabriel.course.projectapi2.jwt.JwtUserDetails;
import com.gabriel.course.projectapi2.model.Client;
import com.gabriel.course.projectapi2.services.ClientService;
import com.gabriel.course.projectapi2.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<ClientResponseDto>> getAllClients() {
        List<Client> clients = clientService.findAllClients();
        return ResponseEntity.ok(ClientMapper.toListDto(clients));
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDto> createClient(@RequestBody @Valid ClientCreatDto client,
                                                          @AuthenticationPrincipal JwtUserDetails userDetails) {
        var c = ClientMapper.toClient(client);
        c.setUser(userService.findById(userDetails.getId()));
        clientService.saveClient(c);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toDto(c));
    }
}
