package com.ensf.fnf.core.dto.requestDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFamilyTreeRequestDto {

    private List<CreateFamilyMemberRequestDto>
            familyMembers;
}