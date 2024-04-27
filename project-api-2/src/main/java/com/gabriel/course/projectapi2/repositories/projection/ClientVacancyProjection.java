package com.gabriel.course.projectapi2.repositories.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ClientVacancyProjection {
    String getMark();
    String getColor();
    String getModel();
    String getPlate();
    String getClientCpf();
    String getReceipt();
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime getEnterDate();
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime getExitDate();
    String getVacancyCode();
    BigDecimal getAmmount();
    BigDecimal getDiscount();

}
