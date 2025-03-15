package com.seminar.seminar.service;

import com.seminar.seminar.model.User;
import com.seminar.seminar.response.DelegateResponse;

import java.util.List;

public interface DelegateService {
    List<User> getAllDelegates();
    DelegateResponse getDelegateById(Long id);
    String updateDelegate(Long id, User user);
    String deleteDelegate(Long id);
}
