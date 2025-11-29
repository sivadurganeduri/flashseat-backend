package com.flashseat.controller;

import com.flashseat.model.Schedule;
import com.flashseat.model.Seat;
import com.flashseat.repository.ScheduleRepository;
import com.flashseat.repository.SeatRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;

    public ScheduleController(ScheduleRepository scheduleRepository, SeatRepository seatRepository) {
        this.scheduleRepository = scheduleRepository;
        this.seatRepository = seatRepository;
    }

    @GetMapping("/{id}/seats")
    public ResponseEntity<List<Seat>> getSeatsForSchedule(@PathVariable String id) {
        List<Seat> seats = seatRepository.findByScheduleId(id);
        return ResponseEntity.ok(seats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getSchedule(@PathVariable String id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found"));
        return ResponseEntity.ok(schedule);
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<?> updateLocation(@PathVariable String id, @RequestBody LocationUpdate request) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found"));
        schedule.setCurrentLat(request.getLat());
        schedule.setCurrentLng(request.getLng());
        schedule.setLastLocationUpdate(java.time.LocalDateTime.now());
        scheduleRepository.save(schedule);
        return ResponseEntity.ok("Location updated");
    }
}

// DTO for location update
class LocationUpdate {
    private Double lat;
    private Double lng;

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }
    public Double getLng() { return lng; }
    public void setLng(Double lng) { this.lng = lng; }
}