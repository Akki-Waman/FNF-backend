package com.ensf.fnf.core.dto.requestDto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AddFamilyMemberRequestDto {

    private String fullName;

    private String relationshipType;

    private String gender;

    private LocalDate birthDate;

    private LocalDate anniversaryDate;

    private Boolean married;

    private String spouseName;

    private LocalDate spouseBirthDate;

    private LocalDate spouseAnniversaryDate;

    private String mobileNumber;

    private String email;
}
