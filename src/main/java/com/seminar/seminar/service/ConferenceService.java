package com.seminar.seminar.service;

import com.seminar.seminar.model.Conference;
import com.seminar.seminar.response.ConferenceResponse;


import java.util.List;

public interface ConferenceService {
    String createConference(Conference conference);
    List<ConferenceResponse> getAllConferences();
    String updateConference(Long id, Conference conference);
    String deleteConference(Long id);
}
