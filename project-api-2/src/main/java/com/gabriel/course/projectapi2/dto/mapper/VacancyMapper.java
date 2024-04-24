package com.gabriel.course.projectapi2.dto.mapper;

import com.gabriel.course.projectapi2.dto.PageableDto;
import com.gabriel.course.projectapi2.dto.Vacancies.VacancyResponseDto;
import com.gabriel.course.projectapi2.model.Vacancy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VacancyMapper {

    public static Vacancy toVacancy(PageableDto.VacancyCreateDto createDto) {
        return  new ModelMapper().map(createDto, Vacancy.class);
    }

    public static VacancyResponseDto toDto(Vacancy vacancy) {
        return  new ModelMapper().map(vacancy, VacancyResponseDto.class);
    }
}
