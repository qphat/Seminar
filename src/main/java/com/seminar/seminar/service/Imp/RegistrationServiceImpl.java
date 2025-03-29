package com.seminar.seminar.service.Imp;

import com.seminar.seminar.domain.RegistrationStatus;
import com.seminar.seminar.domain.Role;
import com.seminar.seminar.model.Conference;
import com.seminar.seminar.model.Registration;
import com.seminar.seminar.model.User;
import com.seminar.seminar.repository.ConferenceRepository;
import com.seminar.seminar.repository.RegistrationRepository;
import com.seminar.seminar.repository.UserRepository;
import com.seminar.seminar.response.*;
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
    @Override
    public RegistrationResponse registerForConference(Long delegateId, Long conferenceId) {
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

        Optional<Registration> existingRegistrationOpt = registrationRepository.findByDelegateIdAndConferenceId(delegateId, conferenceId);
        if (existingRegistrationOpt.isPresent()) {
            Registration existingRegistration = existingRegistrationOpt.get();

            if (existingRegistration.getStatus() == RegistrationStatus.CANCELED) {
                // Nếu đã bị hủy, cập nhật lại thành PENDING
                existingRegistration.setStatus(RegistrationStatus.PENDING);
                registrationRepository.save(existingRegistration);
                return new RegistrationResponse(
                        delegate.getId(),
                        delegate.getFullName(),
                        delegate.getEmail(),
                        RegistrationStatus.PENDING
                );
            } else {
                throw new IllegalArgumentException("Delegate " + delegateId + " is already registered for conference " + conferenceId);
            }
        }

        // Nếu chưa đăng ký trước đó, tạo mới
        Registration registration = new Registration();
        registration.setDelegate(delegate);
        registration.setConference(conference);
        registration.setStatus(RegistrationStatus.PENDING);
        registrationRepository.save(registration);

        return new RegistrationResponse(
                delegate.getId(),
                delegate.getFullName(),
                delegate.getEmail(),
                RegistrationStatus.PENDING
        );
    }


    /**
     * 4.2 Hủy đăng ký hội thảo
     *
     * @param delegateId ID của bản ghi đăng ký
     * @return JSon thông báo thành công
     * @throws IllegalArgumentException nếu không tìm thấy đăng ký
     */
    @Override
    public CancelRegistrationResponse cancelRegistration(Long delegateId, Long conferenceId) {

        // Tìm user từ delegateId
        User currentUser = userRepository.findById(delegateId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + delegateId));

        // Kiểm tra quyền: Chỉ người sở hữu bản ghi mới được hủy
        if (!currentUser.getId().equals(delegateId)) {
            throw new IllegalArgumentException("You are not authorized to cancel this registration");
        }

        // Tìm bản ghi Registration
        Optional<Registration> registrationOpt = registrationRepository.findByDelegateIdAndConferenceId(delegateId, conferenceId);
        if (registrationOpt.isEmpty()) {
            throw new IllegalArgumentException("Registration not found for delegate " + delegateId + " and conference " + conferenceId);
        }

        Registration registration = registrationOpt.get();

        // Kiểm tra trạng thái hiện tại
        if (registration.getStatus() == RegistrationStatus.CANCELED) {
            throw new IllegalArgumentException("Registration is already canceled");
        }

        // Cập nhật trạng thái thành CANCELED
        registration.setStatus(RegistrationStatus.CANCELED);
        registrationRepository.save(registration);

        // Trả về JSON
        return new CancelRegistrationResponse(
                delegateId,
                conferenceId,
                registration.getStatus(),
                "Registration canceled successfully"
        );
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
                .map(r -> new RegistrationResponse(
                        r.getDelegate().getId(),
                        r.getDelegate().getFullName(),
                        r.getDelegate().getEmail(),
                        r.getStatus()
                ))

                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách hội thảo mà một đại biểu đã đăng ký
     * @param delegateId ID của đại biểu
     * @return Danh sách thông tin hội thảo mà đại biểu đã đăng ký (bao gồm trạng thái đăng ký)
     */
    @Override
    public List<ListRegistrationsResponse> getConferencesByDelegate(Long delegateId) {
        // Lấy danh sách registration của delegate
        List<Registration> registrations = registrationRepository.findByDelegateIdAndStatus(delegateId);

        // Chuyển thành danh sách hội thảo với trạng thái từ Registration
        return registrations.stream()
                .map(registration -> {
                    Conference conference = registration.getConference();
                    return new ListRegistrationsResponse(
                            conference.getId(),
                            conference.getTitle(),
                            conference.getStartDate(),
                            conference.getEndDate(),
                            conference.getLocation(),
                            conference.getCapacity(),
                            conference.getRegistrations().size(),
                            registration.getStatus().toString(),
                            conference.getRegistrationDeadline()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistrationDetailsResponse> getAllRegistrations() {
        List<Registration> registrations = registrationRepository.findAll();

        return registrations.stream().map(registration -> {
            Conference conference = registration.getConference();
            User delegate = registration.getDelegate();

            return new RegistrationDetailsResponse(
                    registration.getId(),
                    conference.getId(),
                    conference.getTitle(),
                    delegate.getId(),
                    delegate.getFullName(),
                    delegate.getEmail(),
                    registration.getRegistrationDate(),
                    registration.getStatus().toString()
            );
        }).collect(Collectors.toList());
    }

    /**
     * Cập nhật trạng thái đăng ký của một đại biểu cho một hội thảo
     * @param delegateId ID của đại biểu
     * @param conferenceId ID của hội thảo
     * @param newStatus Trạng thái mới (PENDING, CONFIRMED, hoặc CANCELED)
     * @return Đối tượng UpdateStatusResponse chứa kết quả cập nhật (success/error) và thông báo
     */
    @Override
    public StatusResponse updateRegistrationStatus(Long delegateId, Long conferenceId, String newStatus) {
        // Kiểm tra trạng thái mới hợp lệ
        if (!newStatus.equalsIgnoreCase("PENDING") &&
                !newStatus.equalsIgnoreCase("CONFIRMED") &&
                !newStatus.equalsIgnoreCase("CANCELED")) {
            return new StatusResponse("error", "Invalid status. Must be PENDING, CONFIRMED, or CANCELED");
        }

        // Tìm bản ghi đăng ký
        Optional<Registration> registrationOpt = registrationRepository.findByDelegateIdAndConferenceId(delegateId, conferenceId);
        if (registrationOpt.isEmpty()) {
            return new StatusResponse("error", "Registration not found for delegate " + delegateId + " and conference " + conferenceId);
        }

        Registration registration = registrationOpt.get();

        // Cập nhật trạng thái
        RegistrationStatus updatedStatus = RegistrationStatus.valueOf(newStatus.toUpperCase());
        registration.setStatus(updatedStatus);
        registrationRepository.save(registration);

        return new StatusResponse("success", "Registration status updated to " + newStatus.toUpperCase());
    }
}