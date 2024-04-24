package com.gabriel.course.projectapi2.repositories;

import com.gabriel.course.projectapi2.model.ClientVacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientVacancyRepository extends JpaRepository<ClientVacancy, Long> {
}
