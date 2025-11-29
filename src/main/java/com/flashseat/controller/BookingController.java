package com.flashseat.controller;

import com.flashseat.model.Booking;
import com.flashseat.model.Seat;
import com.flashseat.model.Schedule;
import com.flashseat.model.User;
import com.flashseat.repository.BookingRepository;
import com.flashseat.repository.SeatRepository;
import com.flashseat.repository.ScheduleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;  // ← For querying Schedule object

    public BookingController(BookingRepository bookingRepository, SeatRepository seatRepository, ScheduleRepository scheduleRepository) {
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/my")
    public ResponseEntity<List<Booking>> getMyBookings(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Booking> bookings = bookingRepository.findByUser_IdOrderByBookingDateDesc(user.getId());
        return ResponseEntity.ok(bookings);
    }

    @PostMapping
    public ResponseEntity<?> bookSeat(Authentication authentication, @RequestBody BookingRequest request) {
        User user = (User) authentication.getPrincipal();

        String scheduleId = request.getScheduleId();
        int seatNumber = request.getSeatNumber();

        Seat seat = seatRepository.findByScheduleIdAndSeatNumber(scheduleId, seatNumber)
            .orElseThrow(() -> new RuntimeException("Seat not found for schedule " + scheduleId + ", seat " + seatNumber));

        // Rule 1: Availability
        if (seat.isBooked()) {
            return ResponseEntity.badRequest().body("Seat already booked");
        }

        // Rule 2: Gender check
        if (seat.isLadiesSeat() && user.getGender() != User.Gender.FEMALE) {
            return ResponseEntity.badRequest().body("This seat is for female students only");
        }
        if (!seat.isLadiesSeat() && user.getGender() == User.Gender.FEMALE) {
            return ResponseEntity.badRequest().body("This seat is for male students only");
        }

        // Rule 3: 1 seat per trip per student
        List<Booking> existingBookings = bookingRepository.findByUser_IdAndScheduleIdOrderByBookingDateDesc(user.getId(), scheduleId);
        if (!existingBookings.isEmpty() && existingBookings.get(0).getStatus().equals("CONFIRMED")) {
            return ResponseEntity.badRequest().body("You already have a booking for this trip. Cancel previous seat first.");
        }

        // FIXED: Query full Schedule object using scheduleId
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Schedule not found: " + scheduleId));

        // Book
        seat.setBooked(true);
        seat.setBookedBy(user.getId());
        seatRepository.save(seat);

        Booking booking = Booking.builder()
                .user(user)
                .schedule(schedule)  // ← FIXED: Use queried Schedule object
                .seatNumber(seat.getSeatNumber())
                .status("CONFIRMED")
                .busNumber(schedule.getBus().getBusNumber())
                .routeName(schedule.getBus().getRouteName())
                .tripType(schedule.getTripType())
                .tripDate(schedule.getDate())
                .departureTime(schedule.getDepartureTime().toString())
                .build();
        bookingRepository.save(booking);

        return ResponseEntity.ok("Seat " + seatNumber + " booked successfully!");
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable String id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            return ResponseEntity.badRequest().body("Not your booking");
        }
        if (!booking.getStatus().equals("CONFIRMED")) {
            return ResponseEntity.badRequest().body("Cannot cancel non-confirmed booking");
        }

        // FIXED: Query Seat by scheduleId + seatNumber (no getSeat)
        String scheduleId = booking.getSchedule().getId();
        Seat seat = seatRepository.findByScheduleIdAndSeatNumber(scheduleId, booking.getSeatNumber())
            .orElseThrow(() -> new RuntimeException("Seat not found for cancellation"));

        // Unbook seat
        seat.setBooked(false);
        seat.setBookedBy(null);
        seatRepository.save(seat);

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        return ResponseEntity.ok("Booking cancelled successfully!");
    }
}

// DTO for booking request
class BookingRequest {
    private String scheduleId;
    private int seatNumber;

    public String getScheduleId() { return scheduleId; }
    public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }
    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
}