package com.ensf.fnf.core.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfilesResponseDto {
    private Long userId;
    private String emailAddress;
    private String mobileNumber;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;
    private String profilePhotoUrl;
    private Boolean profileCompleted;
    private Boolean verifiedEmail;
}