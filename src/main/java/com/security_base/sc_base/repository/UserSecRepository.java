package com.security_base.sc_base.repository;

import com.security_base.sc_base.models.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSecRepository extends JpaRepository<UserSec,Long> {
    Optional<UserSec> findUserEntityByUsername(String username);
}
