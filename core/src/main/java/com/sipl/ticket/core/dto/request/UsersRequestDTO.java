package com.sipl.ticket.core.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Users Request DTO")
public class UsersRequestDTO {

    private String firstName;

    private String middleName;

    private String lastName;

    private String userName;

    private String password;

    @Size(max = 10, message = "Phone Number must not exceed 10 digits")
    @Schema(description = "Phone Number", example = "9876543210")
    private String phoneNumber;

    @Email(message = "Invalid Email format")
    @Size(max = 200, message = "Email ID must not exceed 200 characters")
    @Schema(description = "Email ID", example = "contact@abclogistics.com")
    private String emailId;

    private LocalDate dob;

    @Schema(description = "Is Active", example = "true")
    private Boolean isActive;
}
