package com.seminar.seminar.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegistrationDetailsResponse {
    private Long id;
    private Long conferenceId;
    private String conferenceTitle;
    private Long delegateId;
    private String delegateName;
    private String delegateEmail;
    private LocalDateTime registrationDate;
    private String status;

    // Constructor
    public RegistrationDetailsResponse(Long id, Long conferenceId, String conferenceTitle,
                                       Long orgId, String orgName, String orgEmail,
                                       LocalDateTime registrationDate, String status) {
        this.id = id;
        this.conferenceId = conferenceId;
        this.conferenceTitle = conferenceTitle;
        this.delegateId = orgId;
        this.delegateName = orgName;
        this.delegateEmail = orgEmail;
        this.registrationDate = registrationDate;
        this.status = status;
    }
}
