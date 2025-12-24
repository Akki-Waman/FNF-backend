package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommittedOrderCountResponse {
    private Long userId;
    private Long committedOrderCount;
}
