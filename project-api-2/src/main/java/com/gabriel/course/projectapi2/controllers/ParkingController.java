package com.gabriel.course.projectapi2.controllers;

import com.gabriel.course.projectapi2.dto.ParkingCreateDto;
import com.gabriel.course.projectapi2.dto.ParkingReponseDto;
import com.gabriel.course.projectapi2.dto.mapper.ClientVacancyMapper;
import com.gabriel.course.projectapi2.services.ParkingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/parkings")
public class ParkingController {
    @Autowired
    ParkingService parkingService;

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
