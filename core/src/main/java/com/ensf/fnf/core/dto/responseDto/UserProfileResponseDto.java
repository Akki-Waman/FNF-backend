package com.ensf.fnf.core.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDto {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("email_address")
    private String emailAddress;

    @JsonProperty("mobile_number")
    private String mobileNumber;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @JsonProperty("profile_photo_url")
    private String profilePhotoUrl;

    @JsonProperty("profile_completed")
    private Boolean profileCompleted;
}
