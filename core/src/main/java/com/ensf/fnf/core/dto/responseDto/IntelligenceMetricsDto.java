package com.ensf.fnf.core.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntelligenceMetricsDto {
    @JsonProperty("relationship_score")
    private Integer relationshipScore;

    @JsonProperty("people_not_wished")
    private Integer peopleNotWished;

    @JsonProperty("upcoming_celebrations")
    private Integer upcomingCelebrationsCount;

    @JsonProperty("activity_tier")
    private String friendActivityTier;
}