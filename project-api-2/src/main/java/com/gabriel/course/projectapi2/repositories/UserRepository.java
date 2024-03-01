package com.gabriel.course.projectapi2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriel.course.projectapi2.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
