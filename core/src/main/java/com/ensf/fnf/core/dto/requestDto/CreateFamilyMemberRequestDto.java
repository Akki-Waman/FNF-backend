package com.ensf.fnf.core.dto.requestDto;

import com.ensf.fnf.core.enums.RelationshipType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFamilyMemberRequestDto {

    private String fullName;

    private RelationshipType relationshipType;

    private LocalDate birthDate;

    private Boolean married;

    private LocalDate anniversaryDate;

    private String spouseName;

    private LocalDate spouseBirthDate;
}