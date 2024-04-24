package com.gabriel.course.projectapi2.services;

import com.gabriel.course.projectapi2.model.Client;
import com.gabriel.course.projectapi2.model.ClientVacancy;
import com.gabriel.course.projectapi2.model.Vacancy;
import com.gabriel.course.projectapi2.util.ParkingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ParkingService {

    @Autowired
    ClientVacancyService clientVacancyService;

    @Autowired
    ClientService clientService;

    @Autowired
    VacancyService vacancyService;

    @Transactional
    public ClientVacancy checkIn(ClientVacancy clientVacancy) {
        Client client = clientService.findByCpf(clientVacancy.getClient().getCpf());
        clientVacancy.setClient(client);

        Vacancy vacancy = vacancyService.findByVacancyFree();
        vacancy.setStatus(Vacancy.StatusVacancy.OCUPPIED);
        clientVacancy.setVacancy(vacancy);

        clientVacancy.setEnterDate(LocalDateTime.now());
        clientVacancy.setReceipt(ParkingUtils.createReceipt());

        return clientVacancyService.save(clientVacancy);
    }
}
