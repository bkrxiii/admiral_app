package uk.ac.cardiff.phoenix.ilm.scheduledTasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.model.Event;
import uk.ac.cardiff.phoenix.ilm.programs.model.EventStatus;
import uk.ac.cardiff.phoenix.ilm.programs.repository.EventRepository;
import uk.ac.cardiff.phoenix.ilm.programs.service.EventService;
import java.time.LocalDateTime;
import java.util.List;


@Component
public class DailyEventTasks {
    private final EventService eventService;
    private final EventRepository eventRepository;

    @Autowired
    public DailyEventTasks(EventService eventService, EventRepository eventRepository) {
        this.eventService = eventService;
        this.eventRepository = eventRepository;
    }

    //check if event has >4 candidates and is due in 1 week, else cancel event
    @Scheduled(cron = "0 0 0 * * *")
    public void checkEvent() {
        for (Event event : eventRepository.findByEventStatus(EventStatus.PENDING)) {
            List<Candidate> applicants = event.getApplicants();
            int size = (applicants != null) ? applicants.size() : 0;
            if (size < 4 && event.getEventTime().isBefore(event.getEventTime().plusWeeks(1))) {
                System.out.println("Cancelling eventId " + event.getEventId());
                eventService.cancelEvent(event);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void completeEvent() {
        for (Event event : eventRepository.findByEventStatus(EventStatus.PENDING)) {
            if (event.getEventTime().isBefore(LocalDateTime.now())){
                System.out.println("Completing eventId " + event.getEventId());
                eventService.completeEvent(event.getEventId());
            }
        }
    }
}
