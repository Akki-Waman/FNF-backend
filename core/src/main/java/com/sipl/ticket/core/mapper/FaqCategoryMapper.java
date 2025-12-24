package com.sipl.ticket.core.mapper;

import java.util.List;

import com.sipl.ticket.core.dao.entity.FaqCategory;
import com.sipl.ticket.core.dto.response.FaqCategoryDto;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "Spring", uses = AuditUserMasterMapper.class)
public interface FaqCategoryMapper extends AuditEntityMapper {
    @InheritConfiguration(name = "toEntity")
    FaqCategory toEntity(FaqCategoryDto faqCategoryDto);

    @InheritConfiguration(name = "toDto")
    FaqCategoryDto toDto(FaqCategory faqCategory);

    List<FaqCategoryDto> mapFaqCategoryListToDtoList(List<FaqCategory> faqCategoryList);

    default Page<FaqCategoryDto> toDtoPage(Page<FaqCategory> faqCategory) {
        return faqCategory.map(this::toDto);
    }
}