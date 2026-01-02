package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Department;
import com.sipl.ticket.core.dto.request.DepartmentRequestDto;
import com.sipl.ticket.core.dto.response.DepartmentResponseDTO;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AuditUserMasterMapper.class
)
public interface DepartmentMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toEntity")
    Department toEntity(DepartmentRequestDto departmentRequestDto);

    @InheritConfiguration(name = "toDto")
    DepartmentResponseDTO toDto(Department department);

    List<DepartmentResponseDTO> mapDepartmentsListToDtoList(
            List<Department> departments
    );

    List<DepartmentResponseDTO> mapDepartmentsDropListToDtoList(
            List<Department> departments
    );
}
