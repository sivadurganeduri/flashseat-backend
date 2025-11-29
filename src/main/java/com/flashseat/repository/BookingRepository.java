package com.flashseat.repository;

import com.flashseat.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByUser_IdOrderByBookingDateDesc(String userId);
    List<Booking> findByUser_IdAndScheduleIdOrderByBookingDateDesc(String userId, String scheduleId);  // FIXED: scheduleId as string
    List<Booking> findByUser_IdAndStatusOrderByBookingDateDesc(String userId, String status);
    }