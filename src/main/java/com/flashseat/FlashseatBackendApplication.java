package com.flashseat;

import com.flashseat.model.*;
import com.flashseat.repository.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootApplication
public class FlashseatBackendApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(FlashseatBackendApplication.class, args);

        // Connection test
        try {
            MongoTemplate mongoTemplate = ctx.getBean(MongoTemplate.class);
            mongoTemplate.executeCommand("{ ping: 1 }");
            System.out.println("SUCCESS: Connected to FlashSeat MongoDB Atlas database!");
        } catch (Exception e) {
            System.err.println("Failed to connect");
        }

        // Inject repositories
        UserRepository userRepository = ctx.getBean(UserRepository.class);
        BusRepository busRepository = ctx.getBean(BusRepository.class);
        ScheduleRepository scheduleRepository = ctx.getBean(ScheduleRepository.class);
        SeatRepository seatRepository = ctx.getBean(SeatRepository.class);
        BookingRepository bookingRepository = ctx.getBean(BookingRepository.class);
        PasswordEncoder passwordEncoder = ctx.getBean(PasswordEncoder.class);

        String testId = "CSE21001";

        // FIXED: Delete old test user + all bookings to clean slate
        userRepository.findByStudentId(testId).ifPresent(userRepository::delete);
        bookingRepository.findAll().forEach(bookingRepository::delete);  // Delete all old bookings

        // Create test user
        User testUser = User.builder()
                .studentId(testId)
                .name("Siva Durga Prasad")
                .email("siva@college.ac.in")
                .phone("9876543210")
                .department("CSE")
                .year("3")
                .gender(User.Gender.MALE)
                .password(passwordEncoder.encode("password123"))
                .isActive(true)
                .build();

        userRepository.save(testUser);
        String userId = testUser.getId();
        System.out.println("TEST USER CREATED – ID: " + userId);
        System.out.println("Login with:");
        System.out.println("Student ID: CSE21001");
        System.out.println("Password: password123");

        // === MOCK DATA CLEAN SLATE ===
        // Delete old mocks
        busRepository.findAll().forEach(busRepository::delete);
        scheduleRepository.findAll().forEach(scheduleRepository::delete);
        seatRepository.findAll().forEach(seatRepository::delete);

        // Create mock bus
        Bus mockBus = Bus.builder()
                .busNumber("TN07 AB1234")
                .routeName("College → Hostel")
                .totalSeats(50)
                .ladiesRowsFrom(1)
                .ladiesRowsTo(6)
                .isActive(true)
                .build();
        busRepository.save(mockBus);
        String busId = mockBus.getId();
        System.out.println("Mock bus ID: " + busId);

        // Mock schedule
        Schedule mockSchedule = Schedule.builder()
                .bus(mockBus)
                .date(LocalDate.now().plusDays(1))  // FIXED: Future date for Upcoming
                .tripType("MORNING")
                .departureTime(LocalTime.of(7, 15))
                .bookingOpensAt(LocalDate.now().atTime(18, 0))
                .bookingClosesAt(LocalDate.now().plusDays(1).atTime(7, 5))
                .status("UPCOMING")
                .build();
        scheduleRepository.save(mockSchedule);
        String scheduleId = mockSchedule.getId();
        System.out.println("Mock schedule ID: " + scheduleId);  // Use in Postman/frontend

        // Mock seats (50)
        for (int i = 1; i <= 50; i++) {
            boolean isLadiesSeat = i <= 24;
            Seat seat = Seat.builder()
                    .scheduleId(scheduleId)
                    .seatNumber(i)
                    .isLadiesSeat(isLadiesSeat)
                    .isBooked(false)
                    .build();
            seatRepository.save(seat);
        }
        System.out.println("50 mock seats created!");

        // No mock booking – let frontend book first one (no "already booked" on start)
        System.out.println("Mock data ready – no initial booking, user can book first seat!");
        // === END MOCK ===
    }
}