package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TicketTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTagRepository extends JpaRepository<TicketTag, Long> {
}
