package com.gabriel.course.projectapi2.dto.mapper;

import com.gabriel.course.projectapi2.dto.ClientCreatDto;
import com.gabriel.course.projectapi2.dto.ClientResponseDto;
import com.gabriel.course.projectapi2.dto.UserResponseDto;
import com.gabriel.course.projectapi2.model.Client;
import com.gabriel.course.projectapi2.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientMapper {

    public static Client toClient(ClientCreatDto clientCreatDto) {
        return new ModelMapper().map(clientCreatDto, Client.class);

    }

    public static ClientResponseDto toDto(Client client) {
        return new ModelMapper().map(client, ClientResponseDto.class);
    }

    public static List<ClientResponseDto> toListDto(List<Client> clients) {
        return clients.stream()
                .map( u -> toDto(u))
                .collect(Collectors.toList());

    }
}
