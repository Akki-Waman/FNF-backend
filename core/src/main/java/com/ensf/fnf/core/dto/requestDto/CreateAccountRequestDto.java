package com.ensf.fnf.core.dto.requestDto;

import com.ensf.fnf.core.enums.Gender;
import lombok.Data;

@Data
public class CreateAccountRequestDto {

    private String fullName;

    private String mobileNumber;

    private String email;

    private Gender gender;
}