package com.sipl.ticket.core.dto.response;

import lombok.Data;

@Data
public class DmsDocumentDto {
    private Long documentId;
    private String applicationId;
    private String fileName;
    private String originalFileName;
    private String filePath;
    private String fileType;
    private Long fileSizeBytes;
    private String contentHash;
    private Boolean isDeleted;
}
