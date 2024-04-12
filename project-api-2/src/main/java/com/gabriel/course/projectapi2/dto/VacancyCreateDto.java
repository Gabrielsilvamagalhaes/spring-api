package com.gabriel.course.projectapi2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VacancyCreateDto {

    @NotBlank
    @Size(min = 4, max = 4)
    private String code;
    @NotBlank
    @Pattern(regexp = "OCUPPIED|FREE")
    private String status;
}
