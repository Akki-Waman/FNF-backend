package com.sipl.ticket.core.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@NoArgsConstructor
public class DmsUploadResult {

    private Long documentId;
    private Map<String, String> metadata;

    public DmsUploadResult(Long documentId, Map<String, String> metadata) {
        this.documentId = documentId;
        this.metadata = metadata;
    }

}
