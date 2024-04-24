package com.gabriel.course.projectapi2.repositories;

import com.gabriel.course.projectapi2.model.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
   Optional<Vacancy> findByCode(String code);

    Optional<Vacancy> findFirstByStatus(Vacancy.StatusVacancy statusVacancy);
}
