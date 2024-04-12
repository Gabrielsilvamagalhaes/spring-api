package com.gabriel.course.projectapi2.model;

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
@Table(name = "app_vacancy")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class Vacancy implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 4)
    private String code;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusVacancy status;

    public  enum StatusVacancy {
        OCUPPIED, FREE
    }

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
