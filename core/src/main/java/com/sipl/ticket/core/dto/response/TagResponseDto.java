package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TagResponseDto extends AuditDto {
    private Long tagId;

    @NotNull(message = "Tag name is required")
    private String tagName;

    @NotNull(message = "Is active is required")
    private Boolean isActive;

    private Integer branchId;
    private String branchName;
}
