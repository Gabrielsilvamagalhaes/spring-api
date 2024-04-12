package com.gabriel.course.projectapi2.controllers;

import com.gabriel.course.projectapi2.dto.VacancyCreateDto;
import com.gabriel.course.projectapi2.dto.VacancyResponseDto;
import com.gabriel.course.projectapi2.dto.mapper.VacancyMapper;
import com.gabriel.course.projectapi2.services.VacancyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VacancyResponseDto> getByCode(@PathVariable String code) {
        var vacancy = vacancyService.findByCode(code);
        return ResponseEntity.ok(VacancyMapper.toDto(vacancy));
    }

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
