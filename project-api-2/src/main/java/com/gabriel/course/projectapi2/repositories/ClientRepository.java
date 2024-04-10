package com.gabriel.course.projectapi2.repositories;

import com.gabriel.course.projectapi2.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
