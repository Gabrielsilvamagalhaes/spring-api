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

        @NotBlank
        @Size(min = 4, max = 4)
        private String code;
        @NotBlank
        @Pattern(regexp = "OCUPPIED|FREE")
        private String status;
    }
}
