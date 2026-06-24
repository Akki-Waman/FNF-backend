package com.ensf.fnf.core.dto.requestDto;

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
public class CreateProfileRequestDto {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email_address")
    private String emailAddress;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("profile_photo_url")
    private String profilePhotoUrl;
}