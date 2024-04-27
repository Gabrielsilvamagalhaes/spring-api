package com.gabriel.course.projectapi2.repositories;

import com.gabriel.course.projectapi2.model.ClientVacancy;
import com.gabriel.course.projectapi2.repositories.projection.ClientVacancyProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientVacancyRepository extends JpaRepository<ClientVacancy, Long> {
    Optional<ClientVacancy> findByReceiptAndExitDateIsNull(String receipt);

    long countByClientCpfAndExitDateIsNotNull(String cpf);

    List<ClientVacancy> findByClientCpf(String cpf);

    Page<ClientVacancyProjection> findAllByClientCpf(String cpf, Pageable pageable);
}
