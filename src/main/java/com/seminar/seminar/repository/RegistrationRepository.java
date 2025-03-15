package com.seminar.seminar.repository;

import com.seminar.seminar.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Optional<Registration> findByDelegateIdAndConferenceId(Long delegateId, Long conferenceId);

    @Query("SELECT COUNT(r) FROM Registration r WHERE r.conference.id = :conferenceId AND r.status IN ('PENDING', 'CONFIRMED')")
    long countByConferenceIdAndStatus(@Param("conferenceId") Long conferenceId);

    @Query("SELECT COUNT(r) FROM Registration r WHERE r.delegate.id = :delegateId AND r.status IN ('PENDING', 'CONFIRMED')")
    long countByDelegateIdAndStatus(@Param("delegateId") Long delegateId);

    @Query("SELECT r FROM Registration r WHERE r.delegate.id = :delegateId AND r.status IN ('PENDING', 'CONFIRMED')")
    List<Registration> findByDelegateIdAndStatus(@Param("delegateId") Long delegateId);
}
