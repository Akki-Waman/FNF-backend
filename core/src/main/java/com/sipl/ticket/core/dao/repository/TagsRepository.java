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

    @Query(
            "SELECT t FROM Tags t " +
                    "WHERE t.isDelete = false " +
                    "AND ( :isActive IS NULL OR t.isActive = :isActive ) " +
                    "AND ( :query IS NULL OR :query = '' " +
                    "      OR LOWER(t.tagName) LIKE CONCAT('%', LOWER(:query), '%') )"
    )
    Page<Tags> searchTags(
            @Param("query") String query,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );


}
