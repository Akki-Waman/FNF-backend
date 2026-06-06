package com.ensf.fnf.core.dto.requestDto;

import com.ensf.fnf.core.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserProfileRequestDto {

    private String fullName;

    private Gender gender;

    private LocalDate dateOfBirth;

    private Boolean married;

    private String spouseName;

    private LocalDate spouseDob;

    private LocalDate anniversaryDate;
}
