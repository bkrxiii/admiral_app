package uk.ac.cardiff.phoenix.ilm.programs.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.jetbrains.annotations.NotNull;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@NoArgsConstructor
@Audited
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long eventId;
    @OneToOne
    @NotNull
    private Workshop workshopEvent;
    @NotNull
    private LocalDateTime eventTime;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Candidate> applicants;
    @NotNull
    private EventStatus eventStatus;

    // builder pattern in the future.
    public Event(Workshop workshopEvent, LocalDateTime eventTime, List<Candidate> applicants, EventStatus eventStatus) {
        this.workshopEvent = workshopEvent;
        this.eventTime = eventTime;
        this.applicants = applicants;
        this.eventStatus = eventStatus;
    }

    public Event(Workshop workshopEvent, LocalDateTime eventTime, EventStatus eventStatus) {
        this.workshopEvent = workshopEvent;
        this.eventTime = eventTime;
        this.eventStatus = eventStatus;
    }

    public List<Candidate> getApplicants() {
        List<Candidate> applicants = this.applicants;
        if (applicants == null) {
            applicants = new ArrayList<>();
        }
        return applicants;
    }




}
