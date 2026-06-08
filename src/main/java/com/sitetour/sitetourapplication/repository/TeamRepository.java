package com.sitetour.sitetourapplication.repository;

import com.sitetour.sitetourapplication.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TeamRepository extends JpaRepository<Team, Long> {
}