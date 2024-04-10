package com.gabriel.course.projectapi2.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_clients")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;
    @Column(name = "name", nullable = false)
    private String name;


    @OneToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

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

}
