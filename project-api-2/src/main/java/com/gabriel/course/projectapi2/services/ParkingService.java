package com.gabriel.course.projectapi2.services;

import com.gabriel.course.projectapi2.model.Client;
import com.gabriel.course.projectapi2.model.ClientVacancy;
import com.gabriel.course.projectapi2.model.Vacancy;
import com.gabriel.course.projectapi2.util.ParkingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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


    @Transactional
    public ClientVacancy checkOut(String receipt) {
        ClientVacancy clientVacancy = clientVacancyService.findByReceipt(receipt);

        LocalDateTime exitDate = LocalDateTime.now();
        clientVacancy.setExitDate(exitDate);

        BigDecimal ammount = ParkingUtils.calculateCost(clientVacancy.getEnterDate(), exitDate);
        clientVacancy.setAmmount(ammount);

        long totalTimes = clientVacancyService.getNumberForTimesCompletedParking(clientVacancy.getClient().getCpf());
        BigDecimal discount = ParkingUtils.calculateDiscount(ammount, totalTimes);
        clientVacancy.setDiscount(discount);

        clientVacancy.getVacancy().setStatus(Vacancy.StatusVacancy.FREE);
        return clientVacancyService.save(clientVacancy);
    }

}
