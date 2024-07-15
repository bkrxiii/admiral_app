package uk.ac.cardiff.phoenix.ilm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.model.Event;
import uk.ac.cardiff.phoenix.ilm.programs.model.EventStatus;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import uk.ac.cardiff.phoenix.ilm.programs.model.Workshop;
import uk.ac.cardiff.phoenix.ilm.programs.repository.EventRepository;
import uk.ac.cardiff.phoenix.ilm.programs.service.EventService;
import uk.ac.cardiff.phoenix.ilm.programs.service.LevelsService;
import uk.ac.cardiff.phoenix.ilm.programs.service.WorkshopsService;
import uk.ac.cardiff.phoenix.ilm.scheduledTasks.DailyEventTasks;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class EventTests {
    @Autowired
    DailyEventTasks dailyEventTasks;
    @Autowired
    EventService eventService;
    @Autowired
    LevelsService levelsService;
    @Autowired
    WorkshopsService workshopsService;
    @Autowired
    private EventRepository eventRepository;

    // Given that a workshop is pending, when the service cancelEvent is called, then the event is cancelled.
    @AutoConfigureTestDatabase
    @Test
    @DirtiesContext
    public void testEventCancellation() {
        LocalDateTime now = LocalDateTime.now();
        Level level1 = new Level("Level 1", "Level 1 Description", 1);
        Workshop workshop = new Workshop("Workshop 1", "Workshop 1", true, level1);
        Event event = new Event(workshop, now, EventStatus.PENDING);
        levelsService.addLevel(level1);
        workshopsService.addWorkshop(workshop);
        eventService.addEvent(event);
        eventService.cancelEvent(event);
        assert (event.getEventStatus() == EventStatus.CANCELLED);
    }


//    Given that a workshop is scheduled and less than 4 people have applied,
//     when the event is <1 week away, then the event is cancelled.
    @AutoConfigureTestDatabase
    @Test
    @DirtiesContext
    public void testEventCancellationWithLessThan4Applicants() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeek = now.plusWeeks(1);
        Level level1 = new Level("Level 1", "Level 1 Description", 1);
        Workshop workshop = new Workshop("Workshop 1", "Workshop 1", true, level1);
        Event event = new Event(workshop, oneWeek, EventStatus.PENDING);
        levelsService.addLevel(level1);
        workshopsService.addWorkshop(workshop);
        eventService.addEvent(event);
        dailyEventTasks.checkEvent();
        Event checkEvent = eventRepository.findByEventId(event.getEventId());
        assert (checkEvent.getEventStatus() == EventStatus.CANCELLED);
    }

    @AutoConfigureTestDatabase
    @Test
    @DirtiesContext
    public void testOldEventsCompleteAtMidnight() {
        LocalDateTime now = LocalDateTime.now().minus(1, java.time.temporal.ChronoUnit.DAYS);
        Level level1 = new Level("Level 1", "Level 1 Description", 1);
        Workshop workshop = new Workshop("Workshop 1", "Workshop 1", true, level1);
        Event event = new Event(workshop, now, EventStatus.PENDING);
        levelsService.addLevel(level1);
        workshopsService.addWorkshop(workshop);
        eventService.addEvent(event);
        dailyEventTasks.completeEvent();
        Event checkEvent = eventRepository.findByEventId(event.getEventId());
        assert (checkEvent.getEventStatus() == EventStatus.COMPLETED);
    }

    @AutoConfigureTestDatabase
    @Test
    @DirtiesContext
    public void testCompletedEventCantBeCancelled() {
        LocalDateTime now = LocalDateTime.now().minus(1, java.time.temporal.ChronoUnit.DAYS);
        Level level1 = new Level("Level 1", "Level 1 Description", 1);
        Workshop workshop = new Workshop("Workshop 1", "Workshop 1", true, level1);
        Event event = new Event(workshop, now, EventStatus.COMPLETED);
        levelsService.addLevel(level1);
        workshopsService.addWorkshop(workshop);
        eventService.addEvent(event);
        assertThrows(DataIntegrityViolationException.class, () -> {
            eventService.cancelEvent(event);
        });
    }

    @AutoConfigureTestDatabase
    @Test
    @DirtiesContext
    public void testCancelledEventCantBeCompleted() {
        LocalDateTime now = LocalDateTime.now().minus(1, java.time.temporal.ChronoUnit.DAYS);
        Level level1 = new Level("Level 1", "Level 1 Description", 1);
        Workshop workshop = new Workshop("Workshop 1", "Workshop 1", true, level1);
        Event event = new Event(workshop, now, EventStatus.CANCELLED);
        levelsService.addLevel(level1);
        workshopsService.addWorkshop(workshop);
        eventService.addEvent(event);
        assertThrows(DataIntegrityViolationException.class, () -> {
            eventService.completeEvent(event.getEventId());
        });
    }

    @AutoConfigureTestDatabase
    @Test
    @DirtiesContext
    public void testCompletedEventCantRemoveApplicants() {
        LocalDateTime now = LocalDateTime.now().minus(1, java.time.temporal.ChronoUnit.DAYS);
        Level level1 = new Level("Level 1", "Level 1 Description", 1);
        Workshop workshop = new Workshop("Workshop 1", "Workshop 1", true, level1);
        Event event = new Event(workshop, now, EventStatus.PENDING);
        levelsService.addLevel(level1);
        workshopsService.addWorkshop(workshop);
        eventService.addEvent(event);
        Candidate candidate1 = new Candidate("Emily", "Evans", LocalDate.of(2021, 5, 8), 765111, "IT", "Security Engineer", LocalDate.of(2023, 5, 15), 765900L, "emilyevans@aol.com");
        Candidate candidate2 = new Candidate("Frank", "Foster", LocalDate.of(2020, 10, 21), 123333, "Finance", "Financial Analyst", LocalDate.of(2023, 9, 22), 103456L, "frankfoster@aol.com");
        Candidate candidate3 = new Candidate("Grace", "Garrett", LocalDate.of(2019, 7, 4), 567222, "Marketing", "Digital Marketing Specialist", LocalDate.of(2023, 8, 1), 538890L, "gracegarrett@aol.com");
        Candidate candidate4 = new Candidate("Henry", "Harrison", LocalDate.of(2022, 3, 7), 987987, "Operations", "Supply Chain Manager", LocalDate.of(2023, 4, 18), 987104L, "henryharrison@aol.com");
        Candidate candidate5 = new Candidate("Isabella", "Ives", LocalDate.of(2021, 5, 9), 341222, "IT", "Systems Administrator", LocalDate.of(2023, 6, 7), 345108L, "isabellaisles@aol.com");
        Candidate candidate6 = new Candidate("Daniel", "Davis", LocalDate.of(2022, 3, 12), 345777, "Operations", "Operations Manager", LocalDate.of(2023, 2, 28), 348678L, "danieldavis@aol.com");
        eventService.addApplicants(event.getEventId(), candidate1);
        eventService.addApplicants(event.getEventId(), candidate2);
        eventService.addApplicants(event.getEventId(), candidate3);
        eventService.addApplicants(event.getEventId(), candidate4);
        eventService.addApplicants(event.getEventId(), candidate5);
        eventService.addApplicants(event.getEventId(), candidate6);
        eventService.completeEvent(event.getEventId());
        assertThrows(DataIntegrityViolationException.class, () -> {
            eventService.removeApplicants(event.getEventId(), candidate1);
        });

    }


}
