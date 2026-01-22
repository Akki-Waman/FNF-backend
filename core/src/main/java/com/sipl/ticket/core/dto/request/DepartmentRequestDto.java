package com.sipl.ticket.core.dto.request;

import com.sipl.ticket.core.dto.response.BranchDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequestDto {

    private Long departmentId;
    private String departmentName;
    private Boolean isActive;
    private Integer branchId;
    private String branchName;

}
