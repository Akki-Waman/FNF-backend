package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.ClientProducts;
import com.sipl.ticket.core.dao.entity.Contact;
import com.sipl.ticket.core.dto.request.ClientProductsRequestDTO;
import com.sipl.ticket.core.dto.request.ContactRequestDto;
import com.sipl.ticket.core.dto.response.ClientProductsResponseDTO;
import com.sipl.ticket.core.dto.response.ClientResponseDto;
import com.sipl.ticket.core.dto.response.ContactResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {AuditUserMasterMapper.class})
public interface ClientProductMapper{
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "region", ignore = true)
    @Mapping(target = "zone", ignore = true)
    @Mapping(target = "division", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "clientProductId", ignore = true)
    ClientProducts toEntity(ClientProductsRequestDTO clientProductsRequestDTO);


    ClientProductsResponseDTO toDto(ClientProducts clientProducts);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "region", ignore = true)
    @Mapping(target = "zone", ignore = true)
    @Mapping(target = "division", ignore = true)
    @Mapping(target = "unit", ignore = true)
    ClientProducts partialUpdate(
            ClientProductsRequestDTO dto,
            @MappingTarget ClientProducts clientProducts
    );

    List<ClientProductsResponseDTO> toDtoList(List<ClientProducts> clientProducts);
}
