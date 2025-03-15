package com.seminar.seminar.service.Imp;

import com.seminar.seminar.domain.RegistrationStatus;
import com.seminar.seminar.domain.Role;
import com.seminar.seminar.model.Conference;
import com.seminar.seminar.model.Registration;
import com.seminar.seminar.model.User;
import com.seminar.seminar.repository.ConferenceRepository;
import com.seminar.seminar.repository.RegistrationRepository;
import com.seminar.seminar.repository.UserRepository;
import com.seminar.seminar.response.RegistrationResponse;
import com.seminar.seminar.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final ConferenceRepository conferenceRepository;

    /**
     * 4.1 Đại biểu đăng ký tham gia hội thảo
     * @param delegateId ID của đại biểu
     * @param conferenceId ID của hội thảo
     * @return String thông báo thành công
     * @throws IllegalArgumentException nếu không tìm thấy delegate hoặc hội thảo, hoặc vượt capacity
     */
    public String registerForConference(Long delegateId, Long conferenceId) {
        Optional<User> delegateOpt = userRepository.findById(delegateId);
        Optional<Conference> conferenceOpt = conferenceRepository.findById(conferenceId);

        if (delegateOpt.isEmpty()) {
            throw new IllegalArgumentException("Delegate not found with id: " + delegateId);
        }
        if (conferenceOpt.isEmpty()) {
            throw new IllegalArgumentException("Conference not found with id: " + conferenceId);
        }

        User delegate = delegateOpt.get();
        Conference conference = conferenceOpt.get();

        if (delegate.getRole() != Role.DELEGATE) {
            throw new IllegalArgumentException("User with id " + delegateId + " is not a delegate");
        }

        long registeredCount = registrationRepository.countByConferenceIdAndStatus(conferenceId);
        if (registeredCount >= conference.getCapacity()) {
            throw new IllegalArgumentException("Conference capacity exceeded");
        }

        Optional<Registration> existingRegistration = registrationRepository.findByDelegateIdAndConferenceId(delegateId, conferenceId);
        if (existingRegistration.isPresent()) {
            throw new IllegalArgumentException("Delegate " + delegateId + " is already registered for conference " + conferenceId);
        }

        Registration registration = new Registration();
        registration.setDelegate(delegate);
        registration.setConference(conference);
        registration.setStatus(RegistrationStatus.PENDING);
        registrationRepository.save(registration);

        return "Registration successful";
    }

    /**
     * 4.2 Hủy đăng ký hội thảo
     * @param id ID của bản ghi đăng ký
     * @return String thông báo thành công
     * @throws IllegalArgumentException nếu không tìm thấy đăng ký
     */
    public String cancelRegistration(Long id) {
        Optional<Registration> registrationOpt = registrationRepository.findById(id);
        if (registrationOpt.isEmpty()) {
            throw new IllegalArgumentException("Registration not found with id: " + id);
        }

        Registration registration = registrationOpt.get();
        if (registration.getStatus() == RegistrationStatus.CANCELED) {
            throw new IllegalArgumentException("Registration is already canceled");
        }

        registration.setStatus(RegistrationStatus.CANCELED);
        registrationRepository.save(registration);
        return "Registration canceled successfully";
    }

    /**
     * 4.3 Lấy danh sách đại biểu đã đăng ký hội thảo
     * @param conferenceId ID của hội thảo
     * @return List của thông tin đại biểu (delegateId, fullName, email)
     * @throws IllegalArgumentException nếu không tìm thấy hội thảo
     */
    public List<RegistrationResponse> getRegisteredDelegates(Long conferenceId) {
        Optional<Conference> conferenceOpt = conferenceRepository.findById(conferenceId);
        if (conferenceOpt.isEmpty()) {
            throw new IllegalArgumentException("Conference not found with id: " + conferenceId);
        }

        Conference conference = conferenceOpt.get();
        return conference.getRegistrations().stream()
                .filter(r -> r.getStatus() == RegistrationStatus.PENDING || r.getStatus() == RegistrationStatus.CONFIRMED)
                .map(r -> new RegistrationResponse(r.getDelegate().getId(), r.getDelegate().getFullName(), r.getDelegate().getEmail()))
                .collect(Collectors.toList());
    }
}
