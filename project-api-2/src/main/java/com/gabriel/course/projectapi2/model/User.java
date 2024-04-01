package com.gabriel.course.projectapi2.model;

import java.io.Serializable;
import java.time.LocalDateTime;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "app_users")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", nullable = false, unique = true, length = 100)
	private String username;
	@Column(name = "password", nullable = false, length = 200)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, length = 25)
	private Role role = Role.ROLE_CLIENT;

	@CreatedDate
	@Column(name="date_create")
	private LocalDateTime dateCreate;
	@LastModifiedDate
	@Column(name="date_update")
	private LocalDateTime dateUpdate;

	//	Campo usado para registrar o usuário que fez o insert na tabela
	@CreatedBy
	@Column(name="name_create")
	private String nameCreate;
	//	Campo usado para registrar o usuário que fez o update na tabela
	@LastModifiedBy
	@Column(name="name_update")
	private String nameUpdate;



	public User(Long id, String email, String password, Role role, LocalDateTime dateCreate, LocalDateTime dateUpdate,
				String nameCreate, String nameUpdate) {
		this.id = id;
		this.username = email;
		this.password = password;
		this.role = role;
		this.dateCreate = dateCreate;
		this.dateUpdate = dateUpdate;
		this.nameCreate = nameCreate;
		this.nameUpdate = nameUpdate;

	}

	public enum Role {
		ROLE_ADMIN, ROLE_CLIENT
	}

	@Override
	public String toString() {
		return "User [id=" + id + "]";
	}




}