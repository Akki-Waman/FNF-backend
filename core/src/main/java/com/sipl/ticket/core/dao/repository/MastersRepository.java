package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Masters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MastersRepository extends JpaRepository<Masters, Long> {

    List<Masters> findByColumnCode(Integer columnCode);
}
