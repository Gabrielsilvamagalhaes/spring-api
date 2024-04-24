package com.gabriel.course.projectapi2.dto.mapper;

import com.gabriel.course.projectapi2.dto.ParkingCreateDto;
import com.gabriel.course.projectapi2.dto.ParkingReponseDto;
import com.gabriel.course.projectapi2.model.ClientVacancy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientVacancyMapper {

    public static ClientVacancy toClientVacancy(ParkingCreateDto parking) {
        return  new ModelMapper().map(parking, ClientVacancy.class);
    }

    public static ParkingReponseDto toDto(ClientVacancy clientVacancy) {
        return  new ModelMapper().map(clientVacancy, ParkingReponseDto.class);
    }
}
