package com.seminar.seminar.service;

import com.seminar.seminar.model.User;
import com.seminar.seminar.response.DelegateResponse;
import com.seminar.seminar.response.DeleteResponse;
import com.seminar.seminar.response.StatusResponse;

import java.util.List;

public interface DelegateService {
    List<User> getAllDelegates();
    DelegateResponse getDelegateById(Long id);
    StatusResponse updateDelegate(Long id, User user);
    DeleteResponse deleteDelegate(Long id);
    DelegateResponse createDelegate(User user);
}
