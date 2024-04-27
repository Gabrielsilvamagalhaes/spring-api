package com.gabriel.course.projectapi2.dto.mapper;

import com.gabriel.course.projectapi2.dto.ParkingCreateDto;
import com.gabriel.course.projectapi2.dto.ParkingResponseDto;
import com.gabriel.course.projectapi2.model.ClientVacancy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientVacancyMapper {

    public static ClientVacancy toClientVacancy(ParkingCreateDto parking) {
        return  new ModelMapper().map(parking, ClientVacancy.class);
    }

    public static ParkingResponseDto toDto(ClientVacancy clientVacancy) {
        return  new ModelMapper().map(clientVacancy, ParkingResponseDto.class);
    }
}
