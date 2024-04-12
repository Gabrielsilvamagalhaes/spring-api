package com.gabriel.course.projectapi2.repositories;

import com.gabriel.course.projectapi2.model.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
}
