package com.seminar.seminar.service;

import com.seminar.seminar.response.*;

import java.util.List;

public interface RegistrationService {
    RegistrationResponse registerForConference(Long delegateId, Long conferenceId);
    CancelRegistrationResponse cancelRegistration(Long delegateId, Long conferenceId);
    List<RegistrationResponse> getRegisteredDelegates(Long conferenceId);

    List<ListRegistrationsResponse> getConferencesByDelegate(Long delegateId);

    List<RegistrationDetailsResponse> getAllRegistrations();
    StatusResponse updateRegistrationStatus(Long delegateId, Long conferenceId, String newStatus);
}