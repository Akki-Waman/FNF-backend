package com.ensf.fnf.core.dto;

import com.ensf.fnf.core.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginView {

    private Long id;

    private String fullName;

    private String email;

    private String mobileNumber;

    private Gender gender;

    private boolean profileComplete;
}