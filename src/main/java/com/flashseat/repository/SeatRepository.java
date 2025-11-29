package com.flashseat.repository;

import com.flashseat.model.Seat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;

public interface SeatRepository extends MongoRepository<Seat, String> {
    List<Seat> findByScheduleId(String scheduleId);
    List<Seat> findByScheduleIdAndIsBookedFalse(String scheduleId);
    
    @Query("{ 'scheduleId' : ?0, 'seatNumber' : ?1 }")  // FIXED: Exact query for String scheduleId + int seatNumber
    Optional<Seat> findByScheduleIdAndSeatNumber(String scheduleId, int seatNumber);
}