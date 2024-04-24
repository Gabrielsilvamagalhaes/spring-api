package com.gabriel.course.projectapi2.dto.mapper;

import com.gabriel.course.projectapi2.dto.Clients.ClientCreateDto;
import com.gabriel.course.projectapi2.dto.Clients.ClientResponseDto;
import com.gabriel.course.projectapi2.model.Client;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientMapper {

    public static Client toClient(ClientCreateDto clientCreatDto) {
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
