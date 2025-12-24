package com.sipl.ticket.core.dto.request;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserMasterRequestDTO {

    private String firstName;

    private String middleName;

    private String lastName;

    private String userName;

    private String password;

    private String phoneNumber;

    private String emailId;

    @ApiModelProperty(notes = "Indicates whether the user is active or not", example = "true")
    private Boolean isActive;

    private LocalDate dob;
}
