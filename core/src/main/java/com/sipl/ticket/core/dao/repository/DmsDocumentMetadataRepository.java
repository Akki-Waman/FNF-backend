package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.DmsDocumentMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DmsDocumentMetadataRepository extends JpaRepository<DmsDocumentMetadata, Long> {
}
