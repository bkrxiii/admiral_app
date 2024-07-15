package uk.ac.cardiff.phoenix.ilm.programs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.cardiff.phoenix.ilm.programs.model.Event;
import uk.ac.cardiff.phoenix.ilm.programs.model.EventStatus;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByEventId(long eventId);

    List<Event> findByEventStatus(EventStatus eventStatus);
}
