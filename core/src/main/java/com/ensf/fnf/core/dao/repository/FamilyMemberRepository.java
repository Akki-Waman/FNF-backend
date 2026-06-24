package com.ensf.fnf.core.dao.repository;

import com.ensf.fnf.core.dao.entity.FamilyMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
                    "WHERE fm.user.userId = :userId"
    )
    List<FamilyMemberEntity> findByUserId(
            @Param("userId")
            Long userId
    );

    @Query(
            "SELECT fm " +
                    "FROM FamilyMemberEntity fm " +
                    "WHERE fm.familyMemberId = :familyMemberId"
    )
    FamilyMemberEntity findFamilyMemberById(
            @Param("familyMemberId")
            Long familyMemberId
    );

    @Query(
            "SELECT fm " +
                    "FROM FamilyMemberEntity fm " +
                    "WHERE fm.family.familyId = :familyId"
    )
    List<FamilyMemberEntity> findByFamilyId(
            @Param("familyId")
            Long familyId
    );

    @Query(
            "SELECT fm " +
                    "FROM FamilyMemberEntity fm " +
                    "WHERE fm.parentMember.familyMemberId = :parentMemberId"
    )
    List<FamilyMemberEntity> findChildrenByParentId(
            @Param("parentMemberId")
            Long parentMemberId
    );

    @Query(
            "SELECT COUNT(fm) " +
                    "FROM FamilyMemberEntity fm " +
                    "WHERE fm.user.userId = :userId"
    )
    Long countByUserId(
            @Param("userId")
            Long userId
    );
}