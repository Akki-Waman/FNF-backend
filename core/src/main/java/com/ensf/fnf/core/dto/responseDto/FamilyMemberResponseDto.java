package com.ensf.fnf.core.dto.responseDto;

import com.ensf.fnf.core.enums.Gender;
import com.ensf.fnf.core.enums.RelationshipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyMemberResponseDto {

    private Long id;

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

    private Boolean active;
}