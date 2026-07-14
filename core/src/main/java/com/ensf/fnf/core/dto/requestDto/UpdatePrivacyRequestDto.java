package com.ensf.fnf.core.dto.requestDto;
import lombok.Data;
@Data
public class UpdatePrivacyRequestDto {
    private Boolean allowFamilyVisibility;
    private Boolean allowFriendVisibility;
    private Boolean allowRelativeVisibility;
}