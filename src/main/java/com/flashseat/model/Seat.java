package com.flashseat.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "seats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    private String id;

    private String scheduleId;  // FIXED: String instead of @DBRef – easy query

    private int seatNumber;

    private boolean isLadiesSeat;

    private boolean isBooked = false;

    private String bookedBy;  // user.id (String)

    private String bookedByStudentId;  // for fast display

    private String bookedByName;       // "Rahul Kumar"

    private String position;  // "WINDOW", "AISLE", "MIDDLE"
}