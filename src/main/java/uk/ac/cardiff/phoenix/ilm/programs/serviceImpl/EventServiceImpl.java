package uk.ac.cardiff.phoenix.ilm.programs.serviceImpl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.model.Event;
import uk.ac.cardiff.phoenix.ilm.programs.model.EventStatus;
import uk.ac.cardiff.phoenix.ilm.programs.model.Workshop;
import uk.ac.cardiff.phoenix.ilm.programs.repository.EventRepository;
import uk.ac.cardiff.phoenix.ilm.programs.service.EventService;
import java.time.LocalDateTime;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventsRepository;
    public EventServiceImpl(EventRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }


    @Override
    public void addEvent(Workshop workshopEvent, LocalDateTime eventTime, EventStatus eventStatus) {
        eventsRepository.save(new Event(workshopEvent, eventTime, eventStatus));
    }

    @Override
    public void addEvent(Event event) {
        eventsRepository.save(event);
    }

    @Override
    public void addApplicants(long eventId, Candidate candidate) {
    // search for event by id, check event status is pending,
        // check if candidate is already in list and if event candidate list <12,
        // add candidate to list, else throw error
        Event event = eventsRepository.findByEventId(eventId);
        List<Candidate> applicants = eventsRepository.findByEventId(event.getEventId()).getApplicants();
        if (event.getEventStatus() == EventStatus.CANCELLED) {
            throw new DataIntegrityViolationException("Event is cancelled");
        } else if (event.getEventStatus() == EventStatus.COMPLETED) {
            throw new DataIntegrityViolationException("Event is over");
        } else {
            if (applicants != null && applicants.contains(candidate)) {
                throw new DataIntegrityViolationException("Candidate is already in list");
            } else {
                if (applicants == null || applicants.size() < 12){
                    event.getApplicants().add(candidate);
                    eventsRepository.save(event);
                } else {
                    throw new DataIntegrityViolationException("Event is full");
                }
            }
        }
    }

    @Override
    public void removeApplicants(long eventId, Candidate candidate) {
        Event event = eventsRepository.findByEventId(eventId);
        if (event.getEventStatus() == EventStatus.CANCELLED) {
            throw new DataIntegrityViolationException("Event is cancelled");
        } else if (event.getEventStatus() == EventStatus.COMPLETED) {
            throw new DataIntegrityViolationException("Event is over");
        } else {
            if (event.getApplicants().contains(candidate)) {
                event.getApplicants().remove(candidate);
                eventsRepository.save(event);
            } else {
                throw new DataIntegrityViolationException("Candidate is not in list");
            }
        }
    }

    @Override
    public void cancelEvent(Event event) {
        if (event.getEventStatus() == EventStatus.CANCELLED) {
            throw new DataIntegrityViolationException("Event is already cancelled");
        } else if (event.getEventStatus() == EventStatus.COMPLETED) {
            throw new DataIntegrityViolationException("Event is over");
        } else {
            event.setEventStatus(EventStatus.CANCELLED);
            eventsRepository.save(event);
        }
    }

    @Override
    public void completeEvent(long eventId) {
        Event event = eventsRepository.findByEventId(eventId);
        if (event.getEventStatus() == EventStatus.CANCELLED) {
            throw new DataIntegrityViolationException("Event is cancelled");
        } else if (event.getEventStatus() == EventStatus.COMPLETED) {
            throw new DataIntegrityViolationException("Event is already completed");
        } else {
            event.setEventStatus(EventStatus.COMPLETED);
            eventsRepository.save(event);
        }
    }

}
