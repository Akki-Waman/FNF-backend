package com.sipl.ticket.core.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantSearchRequestDTO {
    private String search;

    @Builder.Default
    private Boolean isActive=true;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    @Builder.Default
    private String sortBy = "plantId";

    @Builder.Default
    private String sortDir = "desc";
}
