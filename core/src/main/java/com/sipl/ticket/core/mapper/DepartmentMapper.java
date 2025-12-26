package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Department;
import com.sipl.ticket.core.dto.request.DepartmentRequestDto;
import com.sipl.ticket.core.dto.response.DepartmentResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.WARN,
        componentModel = "spring"
)
public interface DepartmentMapper {

    @Mapping(target = "departmentId", ignore = true)
    Department toEntity(DepartmentRequestDto departmentRequestDto);

    DepartmentResponseDTO toResponseDto(Department department);

    @Mapping(target = "departmentId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Department partialUpdate(
            DepartmentRequestDto departmentRequestDto,
            @MappingTarget Department department
    );

    List<DepartmentResponseDTO> toResponseDtoList(List<Department> departments);
}
