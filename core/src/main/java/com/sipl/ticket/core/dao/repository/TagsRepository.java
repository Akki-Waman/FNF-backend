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

    @Query(
            "select case when count(t) > 0 then true else false end " +
                    "from Tags t " +
                    "where lower(t.tagName) = lower(:tagName) " +
                    "and t.branch.branchId = :branchId"
    )
    boolean existsTagByNameAndBranch(
            @Param("tagName") String tagName,
            @Param("branchId") Integer branchId
    );

    boolean existsByTagNameIgnoreCaseAndTagIdNot(
            String tagName, Long tagId
    );

    @Query(
            "SELECT t FROM Tags t " +
                    "LEFT JOIN t.branch b " +
                    "WHERE t.isDelete = false " +
                    "AND ( :isActive IS NULL OR t.isActive = :isActive ) " +
                    "AND ( :branchId IS NULL OR b.branchId = :branchId ) " +
                    "AND ( :query IS NULL OR :query = '' " +
                    "      OR LOWER(t.tagName) LIKE CONCAT('%', LOWER(:query), '%') )"
    )
    Page<Tags> searchTags(
            @Param("query") String query,
            @Param("isActive") Boolean isActive,
            @Param("branchId") Integer branchId,
            Pageable pageable
    );

    @Query(
            "select case when count(t) > 0 then true else false end " +
                    "from Tags t " +
                    "where lower(t.tagName) = lower(:tagName) " +
                    "and t.branch.branchId = :branchId " +
                    "and t.tagId <> :tagId " +
                    "and t.isDelete = false"
    )
    boolean existsTagByNameAndBranchAndNotSameId(
            @Param("tagName") String tagName,
            @Param("branchId") Integer branchId,
            @Param("tagId") Long tagId
    );

}
