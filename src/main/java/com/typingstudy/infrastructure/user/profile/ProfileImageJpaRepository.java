package com.typingstudy.infrastructure.user.profile;

import com.typingstudy.domain.user.profile.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileImageJpaRepository extends JpaRepository<ProfileImage, Long> {

    @Query(value = "select p from ProfileImage p where p.user.id = :userId")
    Optional<ProfileImage> findByUserId(@Param("userId") Long userId);

    @Query(value = "delete from ProfileImage p where p.user.id = :userId")
    ProfileImage deleteByUserId(@Param("userId") Long userId);
}
