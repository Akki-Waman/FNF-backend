package com.ensf.fnf.core.dto.responseDto;

import com.ensf.fnf.core.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDto {

    private Long id;

    private String fullName;

    private String email;

    private String mobileNumber;

    private Gender gender;

    private LocalDate dateOfBirth;

    private Boolean married;

    private String spouseName;

    private LocalDate spouseDob;

    private LocalDate anniversaryDate;

    private boolean profileComplete;
}
