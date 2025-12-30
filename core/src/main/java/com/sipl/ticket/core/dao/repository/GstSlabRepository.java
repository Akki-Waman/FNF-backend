package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Faq;
import com.sipl.ticket.core.dao.entity.GstSlabMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GstSlabRepository extends JpaRepository<GstSlabMaster, Long> {

    @Query("From GstSlabMaster gst where gst.slabId = :slabId and gst.isActive = true")
    Optional<GstSlabMaster> findActiveById(Long slabId);
}
