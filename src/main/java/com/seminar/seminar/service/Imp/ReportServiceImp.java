package com.seminar.seminar.service.Imp;

import com.seminar.seminar.domain.Role;
import com.seminar.seminar.model.Conference;
import com.seminar.seminar.model.User;
import com.seminar.seminar.repository.ConferenceRepository;
import com.seminar.seminar.repository.RegistrationRepository;
import com.seminar.seminar.repository.UserRepository;
import com.seminar.seminar.response.ConferenceReport;
import com.seminar.seminar.response.DelegateReport;
import com.seminar.seminar.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImp implements ReportService {

    private final ConferenceRepository conferenceRepository;
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;

    /**
     * 6.1 Thống kê số lượng đại biểu tham gia hội thảo
     *
     * @return List<ConferenceReport>
     */
    public List<ConferenceReport> getConferenceStats() {
        List<Conference> conferences = conferenceRepository.findAll();
        return conferences.stream().map(conference -> {
            long totalDelegates = registrationRepository.countByConferenceIdAndStatus(conference.getId());
            return new ConferenceReport(conference.getId(), conference.getTitle(), (int) totalDelegates);
        }).collect(Collectors.toList());
    }

    /**
     * 6.2 Thống kê số lượng hội thảo mà một đại biểu đã tham gia
     * @param delegateId ID của đại biểu
     * @return DelegateReport
     * @throws IllegalArgumentException nếu không tìm thấy đại biểu hoặc không phải ROLE_DELEGATE
     */
    public DelegateReport getDelegateStats(Long delegateId) {
        Optional<User> delegateOpt = userRepository.findById(delegateId);
        if (delegateOpt.isEmpty()) {
            throw new IllegalArgumentException("Delegate not found with id: " + delegateId);
        }

        User delegate = delegateOpt.get();
        if (delegate.getRole() != Role.DELEGATE) {
            throw new IllegalArgumentException("User with id " + delegateId + " is not a delegate");
        }

        long totalConferences = registrationRepository.countByDelegateIdAndStatus(delegateId);
        return new DelegateReport(delegateId, delegate.getFullName(), (int) totalConferences);
    }
}
