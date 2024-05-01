package com.gabriel.course.projectapi2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PageableDto {
    private List content = new ArrayList<>();

    private boolean first;
    private boolean last;

    @JsonProperty("page")
    private int number;
    private int size;
    @JsonProperty("pageElements")
    private int numberOfElements;
    private int totalPages;
    private int totalElements;


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class VacancyCreateDto {

        @NotBlank(message = "{NotBlank.vacancyCreateDto.code}")
        @Size(min = 4, max = 4, message = "{Size.vacancyCreateDto.code}")
        private String code;
        @NotBlank(message = "{NotBlank.vacancyCreateDto.status}")
        @Pattern(regexp = "OCUPPIED|FREE", message = "{Pattern.vacancyCreateDto.status}")
        private String status;
    }
}
