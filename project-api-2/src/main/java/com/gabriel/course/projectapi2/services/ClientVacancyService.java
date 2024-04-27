package com.gabriel.course.projectapi2.services;

import com.gabriel.course.projectapi2.exceptions.EntityNotFoundException;
import com.gabriel.course.projectapi2.model.ClientVacancy;
import com.gabriel.course.projectapi2.repositories.ClientVacancyRepository;
import com.gabriel.course.projectapi2.repositories.projection.ClientVacancyProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientVacancyService {

    @Autowired
    private ClientVacancyRepository repository;

    @Transactional
    public ClientVacancy save(ClientVacancy clientVacancy) {
        return  repository.save(clientVacancy);
    }

    @Transactional(readOnly = true)
    public ClientVacancy findByReceipt(String receipt) {
        return repository.findByReceiptAndExitDateIsNull(receipt).orElseThrow(
                () -> new EntityNotFoundException(String.format("Dados de check-in do recibo: '%s' não encotrado ou  check-out já realizado", receipt))
        );
    }

    @Transactional(readOnly = true)
    public long getNumberForTimesCompletedParking(String cpf) {
        return repository.countByClientCpfAndExitDateIsNotNull(cpf);
    }

    @Transactional(readOnly = true)
    public List<ClientVacancy> getParkings(String cpf) {
        return repository.findByClientCpf(cpf);
    }


    @Transactional(readOnly = true)
    public Page<ClientVacancyProjection> getAllforClientCpf(String cpf, Pageable pageable) {
        return repository.findAllByClientCpf(cpf, pageable);
    }
}

