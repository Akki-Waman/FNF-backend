package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsersResponseDTO{
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String userName;
    private String password;
    private String phoneNumber;
    private String emailId;
    private LocalDate dob;
    private Boolean isActive;
    private String createdBy;
    private LocalDate createdDate;
    private LocalTime createdTime;
    private String modifiedBy;
    private LocalDate modifiedDate;
    private LocalTime modifiedTime;
}
