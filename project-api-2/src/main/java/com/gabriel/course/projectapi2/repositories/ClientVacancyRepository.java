package com.gabriel.course.projectapi2.repositories;

import com.gabriel.course.projectapi2.model.ClientVacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientVacancyRepository extends JpaRepository<ClientVacancy, Long> {
    Optional<ClientVacancy> findByReceiptAndExitDateIsNull(String receipt);
}
