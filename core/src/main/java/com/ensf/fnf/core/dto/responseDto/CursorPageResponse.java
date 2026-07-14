package com.ensf.fnf.core.dto.responseDto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CursorPageResponse<T> {
    private List<T> content;
    private Long nextCursorId;
    private boolean hasNext;

    // Explicit Public Constructors to fix cross-module access errors
    public CursorPageResponse() {}

    public CursorPageResponse(List<T> content, Long nextCursorId, boolean hasNext) {
        this.content = content;
        this.nextCursorId = nextCursorId;
        this.hasNext = hasNext;
    }
}