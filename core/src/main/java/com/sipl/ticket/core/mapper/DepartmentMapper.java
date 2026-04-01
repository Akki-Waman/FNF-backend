package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Department;
import com.sipl.ticket.core.dto.request.DepartmentRequestDto;
import com.sipl.ticket.core.dto.response.DepartmentResponseDTO;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AuditUserMasterMapper.class
)
public interface DepartmentMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toEntity")
    @Mapping(target = "branch", ignore = true)
    Department toEntity(DepartmentRequestDto departmentRequestDto);

    @InheritConfiguration(name = "toDto")
    @Mapping(target = "branchId", source = "branch.branchId")
    @Mapping(target = "branchName", source = "branch.branchName")
    DepartmentResponseDTO toDto(Department department);

    List<DepartmentResponseDTO> mapDepartmentsListToDtoList(
            List<Department> departments
    );

    List<DepartmentResponseDTO> mapDepartmentsDropListToDtoList(
            List<Department> departments
    );
}
