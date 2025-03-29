package com.seminar.seminar.repository;

import com.seminar.seminar.model.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    Optional<Conference> findById(Long id);
}

