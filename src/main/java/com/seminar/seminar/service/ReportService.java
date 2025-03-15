package com.seminar.seminar.service;

import com.seminar.seminar.response.ConferenceReport;
import com.seminar.seminar.response.DelegateReport;

import java.util.List;

public interface ReportService {
    List<ConferenceReport> getConferenceStats();
    DelegateReport getDelegateStats(Long delegateId);
}
