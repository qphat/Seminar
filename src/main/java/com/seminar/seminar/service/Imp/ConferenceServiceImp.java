package com.seminar.seminar.service.Imp;

import com.seminar.seminar.model.Conference;
import com.seminar.seminar.repository.ConferenceRepository;
import com.seminar.seminar.repository.RegistrationRepository;
import com.seminar.seminar.response.ConferenceResponse;
import com.seminar.seminar.service.ConferenceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
        if (conference.getTitle() == null || conference.getDate() == null ||
                conference.getLocation() == null || conference.getCapacity() == null) {
            throw new IllegalArgumentException("Title, date, location, and capacity are required");
        }
        if (conference.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        conferenceRepository.save(conference);
        return "Conference created successfully";
    }

    /**
     * 3.2 Lấy danh sách hội thảo
     * @return List<Conference> với số lượng delegate đã đăng ký
     */
    @Override
    public List<ConferenceResponse> getAllConferences() {
        List<Conference> conferences = conferenceRepository.findAll();
        return conferences.stream().map(conference -> {
            long registeredCount = registrationRepository.countByConferenceIdAndStatus(conference.getId());
            return new ConferenceResponse(
                    conference.getId(),
                    conference.getTitle(),
                    conference.getDate(),
                    conference.getLocation(),
                    conference.getCapacity(),
                    (int) registeredCount
            );
        }).collect(Collectors.toList());
    }

    /**
     * 3.3 Cập nhật thông tin hội thảo
     * @param id ID của hội thảo
     * @param conference Đối tượng chứa thông tin mới
     * @return String thông báo thành công
     * @throws IllegalArgumentException nếu không tìm thấy hội thảo
     */
    @Override
    public String updateConference(Long id, Conference conference) {
        Optional<Conference> existingConference = conferenceRepository.findById(id);
        if (existingConference.isPresent()) {
            Conference conferenceToUpdate = existingConference.get();
            conferenceToUpdate.setTitle(conference.getTitle() != null ? conference.getTitle() : conferenceToUpdate.getTitle());
            conferenceToUpdate.setDescription(conference.getDescription() != null ? conference.getDescription() : conferenceToUpdate.getDescription());
            conferenceToUpdate.setDate(conference.getDate() != null ? conference.getDate() : conferenceToUpdate.getDate());
            conferenceToUpdate.setLocation(conference.getLocation() != null ? conference.getLocation() : conferenceToUpdate.getLocation());
            conferenceToUpdate.setCapacity(conference.getCapacity() != null ? conference.getCapacity() : conferenceToUpdate.getCapacity());
            conferenceRepository.save(conferenceToUpdate);
            return "Conference updated successfully";
        } else {
            throw new IllegalArgumentException("Conference not found with id: " + id);
        }
    }

    /**
     * 3.4 Xóa hội thảo
     * @param id ID của hội thảo
     * @return String thông báo thành công
     * @throws IllegalArgumentException nếu không tìm thấy hội thảo
     */
    @Override
    public String deleteConference(Long id) {
        Optional<Conference> existingConference = conferenceRepository.findById(id);
        if (existingConference.isPresent()) {
            conferenceRepository.deleteById(id);
            return "Conference deleted successfully";
        } else {
            throw new IllegalArgumentException("Conference not found with id: " + id);
        }
    }
}

