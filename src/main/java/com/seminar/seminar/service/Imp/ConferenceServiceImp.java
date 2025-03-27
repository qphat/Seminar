package com.seminar.seminar.service.Imp;

import com.seminar.seminar.domain.ConferenceStatus;
import com.seminar.seminar.model.Conference;
import com.seminar.seminar.repository.ConferenceRepository;
import com.seminar.seminar.repository.RegistrationRepository;
import com.seminar.seminar.response.ConferenceResponse;
import com.seminar.seminar.response.DeleteResponse;
import com.seminar.seminar.service.ConferenceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ConferenceServiceImp implements ConferenceService {

    private final ConferenceRepository conferenceRepository;
    private final RegistrationRepository registrationRepository;

    @Override
    public String createConference(Conference conference) {
        if (conference.getTitle() == null ||
                conference.getStartDate() == null ||
                conference.getEndDate() == null ||
                conference.getLocation() == null ||
                conference.getCapacity() == null ||
                conference.getRegistrationDeadline() == null) { // Kiểm tra registrationDeadline
            throw new IllegalArgumentException("Title, dates, location, capacity, and registration deadline are required");
        }

        if (conference.getEndDate().isBefore(conference.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        if (conference.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }

        // Kiểm tra registrationDeadline phải trước startDate
        if (conference.getRegistrationDeadline().isAfter(conference.getStartDate())) {
            throw new IllegalArgumentException("Registration deadline must be before start date");
        }

        conferenceRepository.save(conference);
        return "Conference created successfully";
    }

    @Override
    public List<ConferenceResponse> getAllConferences() {
        List<Conference> conferences = conferenceRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        return conferences.stream().map(conference -> {
            long registeredCount = registrationRepository.countByConferenceIdAndStatus(conference.getId());
            ConferenceStatus status = conference.getStartDate().isAfter(now)
                    ? ConferenceStatus.UPCOMING
                    : ConferenceStatus.COMPLETED;

            return new ConferenceResponse(
                    conference.getId(),
                    conference.getTitle(),
                    conference.getStartDate(),
                    conference.getEndDate(),
                    conference.getLocation(),
                    conference.getCapacity(),
                    (int) registeredCount,
                    status,
                    conference.getRegistrationDeadline() // Thêm registrationDeadline
            );
        }).collect(Collectors.toList());
    }

    @Override
    public String updateConference(Long id, Conference conference) {
        Optional<Conference> existingConference = conferenceRepository.findById(id);
        if (existingConference.isEmpty()) {
            throw new IllegalArgumentException("Conference not found with id: " + id);
        }

        Conference conferenceToUpdate = existingConference.get();

        conferenceToUpdate.setTitle(
                conference.getTitle() != null ? conference.getTitle() : conferenceToUpdate.getTitle());
        conferenceToUpdate.setDescription(
                conference.getDescription() != null ? conference.getDescription() : conferenceToUpdate.getDescription());
        conferenceToUpdate.setStartDate(
                conference.getStartDate() != null ? conference.getStartDate() : conferenceToUpdate.getStartDate());
        conferenceToUpdate.setEndDate(
                conference.getEndDate() != null ? conference.getEndDate() : conferenceToUpdate.getEndDate());
        conferenceToUpdate.setLocation(
                conference.getLocation() != null ? conference.getLocation() : conferenceToUpdate.getLocation());
        conferenceToUpdate.setCapacity(
                conference.getCapacity() != null ? conference.getCapacity() : conferenceToUpdate.getCapacity());
        conferenceToUpdate.setRegistrationDeadline(
                conference.getRegistrationDeadline() != null ? conference.getRegistrationDeadline() : conferenceToUpdate.getRegistrationDeadline()); // Thêm registrationDeadline

        if (conferenceToUpdate.getEndDate().isBefore(conferenceToUpdate.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        if (conferenceToUpdate.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }

        // Kiểm tra registrationDeadline phải trước startDate
        if (conferenceToUpdate.getRegistrationDeadline().isAfter(conferenceToUpdate.getStartDate())) {
            throw new IllegalArgumentException("Registration deadline must be before start date");
        }



        conferenceRepository.save(conferenceToUpdate);
        return "Conference updated successfully";
    }

    @Override
    public ConferenceResponse getConferenceById(Long id) {
        Optional<Conference> conferenceOpt = conferenceRepository.findById(id);
        if (conferenceOpt.isEmpty()) {
            throw new IllegalArgumentException("Conference not found with id: " + id);
        }

        Conference conference = conferenceOpt.get();
        long registeredCount = registrationRepository.countByConferenceIdAndStatus(conference.getId());
        LocalDateTime now = LocalDateTime.now();
        ConferenceStatus status = conference.getStartDate().isAfter(now)
                ? ConferenceStatus.UPCOMING
                : ConferenceStatus.COMPLETED;

        return new ConferenceResponse(
                conference.getId(),
                conference.getTitle(),
                conference.getStartDate(),
                conference.getEndDate(),
                conference.getLocation(),
                conference.getCapacity(),
                (int) registeredCount,
                status,
                conference.getRegistrationDeadline()
        );
    }

    @Override
    public DeleteResponse deleteConference(Long id) {
        Optional<Conference> existingConference = conferenceRepository.findById(id);
        if (existingConference.isPresent()) {
            conferenceRepository.deleteById(id);
            return new DeleteResponse("success", "Conference deleted successfully");
        } else {
            throw new IllegalArgumentException("Conference not found with id: " + id);
        }
    }
}
