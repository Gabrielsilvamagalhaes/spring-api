package com.gabriel.course.projectapi2.model;

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

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_client_vacancy")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class ClientVacancy implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number_receipt", nullable = false, unique = true, length = 15)
    private String receipt;
    @Column(name = "mark", nullable = false, length = 45)
    private String mark;
    @Column(name = "color", nullable = false, length = 45)
    private String color;
    @Column(name = "model", nullable = false, length = 45)
    private String model;
    @Column(name = "plate", nullable = false, length = 8)
    private String plate;

    @Column(name = "date_enter", nullable = false)
    private LocalDateTime enterDate;
    @Column(name = "date_exit")
    private  LocalDateTime exitDate;

    @Column(name = "ammount", columnDefinition = "decimal(7,2)")
    private BigDecimal ammount;
    @Column(name = "discount", columnDefinition = "decimal(7,2)")
    private BigDecimal discount;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_vacancy", nullable = false)
    private Vacancy vacancy;

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
