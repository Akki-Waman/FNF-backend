package com.sipl.ticket.core.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User Request DTO")
public class UserRequestDTO {
    private UserMasterRequestDTO userMasterRequestDTO;
    private Long roleId;
    private String createdBy;
    private List<Long> plantIds;
}
