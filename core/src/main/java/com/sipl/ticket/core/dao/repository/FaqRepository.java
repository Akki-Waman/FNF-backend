package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Faq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Integer> {

    @Query(
            "FROM Faq f WHERE "
                    + "f.isActive = true AND "
                    + "f.answer IS NOT NULL AND "
                    + "(:question IS NULL OR LOWER(f.question) LIKE LOWER(CONCAT('%', :question, '%'))) AND "
                    + "(:categoryId IS NULL OR f.faqCategory.faqCategoryId = :categoryId)")
    Page<Faq> findBySearchQuery(
            @Param("question") String question,
            @Param("categoryId") Integer categoryId,
            Pageable pageable);
}
