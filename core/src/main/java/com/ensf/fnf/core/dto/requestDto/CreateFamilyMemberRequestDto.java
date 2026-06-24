package com.ensf.fnf.core.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFamilyMemberRequestDto {

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