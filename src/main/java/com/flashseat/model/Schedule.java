package com.flashseat.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.*;
import java.util.List;

@Document(collection = "schedules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    @Id
    private String id;

    @DBRef
    private Bus bus;  // reference to bus

    private LocalDate date;  // e.g., 2025-11-23

    private String tripType;  // "MORNING" or "EVENING"

    private LocalTime departureTime;  // e.g., 07:15 or 17:30

    private LocalDateTime bookingOpensAt;   // auto: previous day 6 PM or same day 10 AM
    private LocalDateTime bookingClosesAt;  // departure - 10 minutes

    private String status = "UPCOMING";  // UPCOMING, RUNNING, COMPLETED, CANCELLED

    // Driver real-time tracking fields
    private Double currentLat;
    private Double currentLng;
    private LocalDateTime lastLocationUpdate;

    private LocalDateTime createdAt = LocalDateTime.now();
}