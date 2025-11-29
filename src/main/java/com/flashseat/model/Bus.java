package com.flashseat.model;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "buses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bus {

    @Id
    private String id;

    @NotBlank(message = "Bus number is required")
    private String busNumber;  // e.g., TN07 AB1234

    @NotBlank
    private String routeName;  // e.g., "College → Girls Hostel"

    private String driverName;

    private String driverPhone;

    @Min(30)
    @Max(70)
    private int totalSeats = 50;  // default 50

    @Min(1)
    private int ladiesRowsFrom = 1;   // Front rows for ladies

    @Min(5)
    private int ladiesRowsTo = 6;     // Rows 1-6 ladies, rest gents

    private boolean isActive = true;
}