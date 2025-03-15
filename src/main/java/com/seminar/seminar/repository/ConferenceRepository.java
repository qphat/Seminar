package com.seminar.seminar.repository;

import com.seminar.seminar.model.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {
}

