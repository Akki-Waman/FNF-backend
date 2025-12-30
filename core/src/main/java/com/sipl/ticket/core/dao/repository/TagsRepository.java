package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Tags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Long> {

    boolean existsByTagNameIgnoreCase(String tagName);

    boolean existsByTagNameIgnoreCaseAndTagIdNot(
            String tagName, Long tagId
    );

    @Query("SELECT t " +
            "FROM Tags t " +
            "WHERE t.isActive = true " +
            "AND (:tagId IS NULL OR t.tagId = :tagId)")
    Page<Tags> searchByTagId(
            @Param("tagId") Long tagId,
            Pageable pageable
    );
}
