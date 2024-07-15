package uk.ac.cardiff.phoenix.ilm.programs.service;

import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.model.Event;
import uk.ac.cardiff.phoenix.ilm.programs.model.EventStatus;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import uk.ac.cardiff.phoenix.ilm.programs.model.Workshop;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface EventService {

    void addEvent(Workshop workshopEvent, LocalDateTime eventTime, EventStatus eventStatus);
    void addEvent(Event event);
    void addApplicants(long eventId, Candidate candidate);
    void removeApplicants(long eventId, Candidate candidate);
    void cancelEvent(Event event);

    void completeEvent(long eventId);

}
