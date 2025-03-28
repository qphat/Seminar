package com.seminar.seminar.service;

import com.seminar.seminar.model.Conference;
import com.seminar.seminar.response.ConferenceResponse;
import com.seminar.seminar.response.DeleteResponse;
import com.seminar.seminar.response.StatusResponse;


import java.util.List;

public interface ConferenceService {
    StatusResponse createConference(Conference conference);
    List<ConferenceResponse> getAllConferences();
    String updateConference(Long id, Conference conference);
    DeleteResponse deleteConference(Long id);
    ConferenceResponse getConferenceById(Long id);
}
