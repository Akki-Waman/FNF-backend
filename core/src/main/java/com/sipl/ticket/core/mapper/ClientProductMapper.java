package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.ClientProducts;
import com.sipl.ticket.core.dao.entity.Contact;
import com.sipl.ticket.core.dto.request.ContactRequestDto;
import com.sipl.ticket.core.dto.response.ClientProductsResponseDTO;
import com.sipl.ticket.core.dto.response.ClientResponseDto;
import com.sipl.ticket.core.dto.response.ContactResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {AuditUserMasterMapper.class})
public interface ClientProductMapper{
    ClientProducts toEntity(ClientProductsResponseDTO dto);

    ClientProductsResponseDTO toDto(ClientProducts clientProducts);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClientProducts partialUpdate(
            ClientProductsResponseDTO dto,
            @MappingTarget ClientProducts clientProducts
    );

    List<ClientProductsResponseDTO> toDtoList(List<ClientProducts> clientProducts);
}
