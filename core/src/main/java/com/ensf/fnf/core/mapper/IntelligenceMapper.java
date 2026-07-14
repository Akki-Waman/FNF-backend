package com.ensf.fnf.core.mapper;

import com.ensf.fnf.core.dao.entity.RelationshipIntelligenceEntity;
import com.ensf.fnf.core.dto.responseDto.IntelligenceMetricsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IntelligenceMapper {
    IntelligenceMetricsDto toDto(RelationshipIntelligenceEntity entity);
}