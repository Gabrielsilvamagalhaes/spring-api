package com.gabriel.course.projectapi2.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.gabriel.course.projectapi2.model.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "app_users")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "username", nullable = false, unique = true, length = 100)
	private String email;
	@Column(name = "password", nullable = false, length = 200)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, length = 25)
	private Role role;
	
	@Column(name="date_create")
	private LocalDateTime dateCreate;
	@Column(name="date_update")
	private LocalDateTime dateUpdate;
	
//	Campo usado para registrar o usuário que fez o insert na tabela
	@Column(name="name_create")
	private String nameCreate;
//	Campo usado para registrar o usuário que fez o update na tabela
	@Column(name="name_update")
	private String nameUpdate;
	
	public User(Long id, String email, String password, Role role, LocalDateTime dateCreate, LocalDateTime dateUpdate,
			String nameCreate, String nameUpdate) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.role = role;
		this.dateCreate = dateCreate;
		this.dateUpdate = dateUpdate;
		this.nameCreate = nameCreate;
		this.nameUpdate = nameUpdate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + "]";
	}
	
	
	

}
