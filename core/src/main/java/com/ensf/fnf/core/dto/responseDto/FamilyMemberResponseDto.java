package com.ensf.fnf.core.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("family_member_id")
    private Long familyMemberId;

    @JsonProperty("member_name")
    private String memberName;

    @JsonProperty("relationship_type")
    private String relationshipType;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @JsonProperty("profile_photo_url")
    private String profilePhotoUrl;

    @JsonProperty("parent_member_id")
    private Long parentMemberId;
}