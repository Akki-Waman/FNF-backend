package com.sipl.ticket.core.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "User details")
public class UserMasterResponseDTO extends AuditDto {
    @ApiModelProperty(notes = "Id of user")
    private Long id;

    @ApiModelProperty(notes = "The first name of the user", example = "John")
    private String firstName;

    @ApiModelProperty(notes = "The middle name of the user", example = "William")
    private String middleName;

    @ApiModelProperty(notes = "The last name of the user", example = "Doe")
    private String lastName;

    @ApiModelProperty(notes = "Username of user without space", example = "John192")
    private String userName;

    @ApiModelProperty(notes = "The password of the user", example = "*****")
    private String password;

    @ApiModelProperty(notes = "The phone number of the user", example = "1234567890")
    private String phoneNumber;

    @ApiModelProperty(notes = "The email address of the user", example = "john.doe@example.com")
    private String emailId;

    @ApiModelProperty(notes = "Indicates whether the user is active or not", example = "true")
    private Boolean isActive = false;

    @ApiModelProperty(notes = "The date of birth of the user", example = "1990-01-01")
    private LocalDate dob;

    private String rawPassword;

    private List<UserRoleResponseDTO> userRoleResponseDTO;

    private List<TransporterResponseDTO> transporterResponseDTO;
}
