package com.ensf.fnf.core.dto.requestDto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserProfileRequestDto {

    private String firstName;

    private String lastName;

    private String gender;

    private LocalDate dateOfBirth;

    private Boolean married;

    private String spouseName;

    private LocalDate spouseDob;

    private LocalDate anniversaryDate;
}
