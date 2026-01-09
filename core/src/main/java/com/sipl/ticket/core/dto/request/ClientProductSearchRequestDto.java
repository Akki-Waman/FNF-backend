package com.sipl.ticket.core.dto.request;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientProductSearchRequestDto extends SearchRequestDto{
    private String query;
}
