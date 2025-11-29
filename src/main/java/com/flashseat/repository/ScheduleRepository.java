package com.flashseat.repository;

import com.flashseat.model.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    List<Schedule> findByDateAndTripType(LocalDate date, String tripType);
    List<Schedule> findByDate(LocalDate date);
}