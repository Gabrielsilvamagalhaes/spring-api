package com.gabriel.course.projectapi2.services;

import com.gabriel.course.projectapi2.exceptions.CodeUniqueViolationException;
import com.gabriel.course.projectapi2.exceptions.EntityNotFoundException;
import com.gabriel.course.projectapi2.model.Vacancy;
import com.gabriel.course.projectapi2.repositories.ClientRepository;
import com.gabriel.course.projectapi2.repositories.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VacancyService {

    @Autowired
    VacancyRepository vacancyRepository;

    @Transactional
    public Vacancy save(Vacancy vacancy) {
        try {
            return vacancyRepository.save(vacancy);
        } catch (DataIntegrityViolationException exception) {
            throw new CodeUniqueViolationException(String.format("A vaga com o código ('%s') ja cadastrada!", vacancy.getCode()));
        }
    }

    @Transactional(readOnly = true)
    public Vacancy findByCode(String code) {
        return vacancyRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException(String.format("Vaga com o código ('%s') não foi encontrada", code)));
    }
}
