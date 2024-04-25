package com.gabriel.course.projectapi2.services;

import com.gabriel.course.projectapi2.model.ClientVacancy;
import com.gabriel.course.projectapi2.repositories.ClientVacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientVacancyService {

    @Autowired
    private ClientVacancyRepository repository;

    @Transactional
    public ClientVacancy save(ClientVacancy clientVacancy) {
        return  repository.save(clientVacancy);
    }
}
