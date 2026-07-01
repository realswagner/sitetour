package com.sitetour.sitetourapplication.repository;

import com.sitetour.sitetourapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {


    Optional<User> findByUsername(String username);

    List<User> findByTeamId(Long teamId);

    List<User> findAllByTeamId(Long teamId);

    void deleteAllByTeamId(Long teamId);
}