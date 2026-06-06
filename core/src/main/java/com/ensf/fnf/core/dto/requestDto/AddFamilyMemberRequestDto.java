package com.ensf.fnf.core.dto.requestDto;

import com.ensf.fnf.core.enums.Gender;
import com.ensf.fnf.core.enums.RelationshipType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AddFamilyMemberRequestDto {

    private String fullName;

    private RelationshipType relationshipType;

    private Gender gender;

    private LocalDate birthDate;

    private LocalDate anniversaryDate;

    private Boolean married;

    private String spouseName;

    private LocalDate spouseBirthDate;

    private LocalDate spouseAnniversaryDate;

    private String mobileNumber;

    private String email;
}
