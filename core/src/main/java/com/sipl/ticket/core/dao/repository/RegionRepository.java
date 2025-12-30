package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
}
