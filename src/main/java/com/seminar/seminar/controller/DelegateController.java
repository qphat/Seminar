package com.seminar.seminar.controller;

import com.seminar.seminar.model.User;
import com.seminar.seminar.response.DelegateResponse;
import com.seminar.seminar.response.DeleteResponse;
import com.seminar.seminar.response.StatusResponse;
import com.seminar.seminar.service.DelegateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delegates")
public class DelegateController {


    private final DelegateService delegateService;

    @GetMapping
    public ResponseEntity<List<User>> getAllDelegates() {
        List<User> delegates = delegateService.getAllDelegates();
        return ResponseEntity.ok(delegates);
    }

    /**
     * 2.4 Lấy thông tin đại biểu theo ID
     * @param id ID của đại biểu
     * @return DelegateResponse
     */
    @GetMapping("/{id}")
    public ResponseEntity<DelegateResponse> getDelegateById(@PathVariable Long id) {
        DelegateResponse delegate = delegateService.getDelegateById(id);
        return ResponseEntity.ok(delegate);
    }

    @PutMapping("/{id}")
    public StatusResponse updateDelegate(@PathVariable Long id, @RequestBody User user) {
        try {
            return delegateService.updateDelegate(id, user);
        } catch (IllegalArgumentException e) {
            return new StatusResponse("error", e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteDelegate(@PathVariable Long id) {
        DeleteResponse response = delegateService.deleteDelegate(id);
        return ResponseEntity.ok(response);
    }


    @PostMapping()
    public ResponseEntity<DelegateResponse> createDelegate(@RequestBody User user) {
        DelegateResponse response = delegateService.createDelegate(user);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNotFound(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
