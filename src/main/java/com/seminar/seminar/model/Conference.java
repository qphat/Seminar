package com.seminar.seminar.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Conference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 255)
    private String location;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "conference", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Registration> registrations = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public Conference(String title, String description, LocalDate date, String location, Integer capacity) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.capacity = capacity;
        this.createdAt = LocalDateTime.now();
        this.registrations = new ArrayList<>();
    }
}