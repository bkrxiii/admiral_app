package uk.ac.cardiff.phoenix.ilm.programs.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;

@Data
@Entity
@NoArgsConstructor
@Audited
public class Homework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long homeworkId;
    @ManyToOne
    private Event event;
    @ManyToOne
    private Candidate candidate;
    private Boolean completed;

    public Homework(Event event, Candidate candidate, Boolean completed) {
        this.event = event;
        this.candidate = candidate;
        this.completed = completed;
    }
}
