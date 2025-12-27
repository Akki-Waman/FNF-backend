package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Client;
import com.sipl.ticket.core.dto.request.ClientRequestDto;
import com.sipl.ticket.core.dto.response.ClientResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.WARN,
        componentModel = "spring"
)
public interface ClientMapper {


    @Mapping(target = "clientId", ignore = true)
    Client toEntity(ClientRequestDto dto);


    ClientResponseDto toDto(Client client);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "clientId", ignore = true)
    void partialUpdate(
            ClientRequestDto dto,
            @MappingTarget Client client
    );


    List<ClientResponseDto> toDtoList(List<Client> clients);

    default List<ClientResponseDto> mapClientListToDtoList(List<Client> clients) {
        return toDtoList(clients);
    }
}
