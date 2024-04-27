package com.gabriel.course.projectapi2.dto.mapper;

import com.gabriel.course.projectapi2.dto.ParkingCreateDto;
import com.gabriel.course.projectapi2.dto.ParkingResponseDto;
import com.gabriel.course.projectapi2.dto.Users.UserResponseDto;
import com.gabriel.course.projectapi2.model.ClientVacancy;
import com.gabriel.course.projectapi2.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientVacancyMapper {

    public static List<ParkingResponseDto> toListDto(List<ClientVacancy> clientVacancys) {
        return clientVacancys.stream()
                .map( c -> toDto(c))
                .collect(Collectors.toList());

    }

    public static ClientVacancy toClientVacancy(ParkingCreateDto parking) {
        return  new ModelMapper().map(parking, ClientVacancy.class);
    }

    public static ParkingResponseDto toDto(ClientVacancy clientVacancy) {
        return  new ModelMapper().map(clientVacancy, ParkingResponseDto.class);
    }
}
