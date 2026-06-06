package com.ensf.fnf.core.dto.responseDto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonApiResponse<T> {

    private boolean success;

    private String message;

    private T data;
}