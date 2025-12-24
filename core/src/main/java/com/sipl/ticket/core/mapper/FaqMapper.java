package com.sipl.ticket.core.mapper;

import java.util.List;

import com.sipl.ticket.core.dao.entity.Faq;
import com.sipl.ticket.core.dto.response.FaqDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", uses = AuditUserMasterMapper.class)
public interface FaqMapper extends AuditEntityMapper {
    @InheritConfiguration(name = "toEntity")
    Faq toEntity(FaqDto faqDto);

    @InheritConfiguration(name = "toDto")
    FaqDto toDto(Faq faq);

    List<FaqDto> mapFaqListToDtoList(List<Faq> faqList);

    default Page<FaqDto> toDtoPage(Page<Faq> faq) {
        return faq.map(this::toDto);
    }
}
