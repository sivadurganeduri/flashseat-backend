package com.flashseat.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "bookings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    private String id;

    @DBRef
    private User user;        // who booked

    @DBRef
    private Schedule schedule; // which trip

    private int seatNumber;

    private LocalDateTime bookingDate = LocalDateTime.now();

    private String status = "CONFIRMED";  // CONFIRMED, CANCELLED

    private boolean ticketDownloaded = false;  // for analytics (optional)

    // For fast display in My Bookings (no join needed)
    private String busNumber;
    private String routeName;
    private String tripType;      // MORNING / EVENING
    private LocalDate tripDate;
    private String departureTime;
}