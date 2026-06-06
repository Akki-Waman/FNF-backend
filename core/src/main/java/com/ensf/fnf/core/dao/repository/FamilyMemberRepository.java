package com.ensf.fnf.core.dao.repository;

import com.ensf.fnf.core.dao.entity.FamilyMemberEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyMemberRepository
        extends JpaRepository<
        FamilyMemberEntity,
        Long> {

    @Query(
            "SELECT fm " +
                    "FROM FamilyMemberEntity fm " +
                    "WHERE fm.user.id = :userId " +
                    "AND fm.active = true"
    )
    List<FamilyMemberEntity>
    findByUserId(
            @Param("userId")
            Long userId
    );
}