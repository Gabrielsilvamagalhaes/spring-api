package com.gabriel.course.projectapi2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingService {

    @Autowired
    ClientVacancyService clientVacancyService;

    @Autowired
    ClientService clientService;

    @Autowired
    VacancyService vacancyService;
}
