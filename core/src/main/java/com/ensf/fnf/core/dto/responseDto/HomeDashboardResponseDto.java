package com.ensf.fnf.core.dto.responseDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeDashboardResponseDto {

    private Long userId;

    private String fullName;

    private String email;

    private String mobileNumber;

    private List<FamilyMemberResponseDto>
            familyMembers;
}