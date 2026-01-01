package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.DmsDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DmsDocumentRepository extends JpaRepository<DmsDocument, Long> {
}
