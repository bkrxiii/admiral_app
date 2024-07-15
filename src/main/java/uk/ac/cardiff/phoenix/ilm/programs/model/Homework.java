// Ai was used as an inspiration
package uk.ac.cardiff.phoenix.ilm.programs.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Data
@Entity
@NoArgsConstructor
@Audited
public class Homework {
    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    // The candidate associated with the homework

    private Long candidateId;

    // Boolean to indicate whether the homework has been completed or not
    @Column(nullable = false)
    private Boolean completed;
    // The workshop associated with the homework
    @ManyToOne
    private Workshop workshop;

    // Constructor
    public Homework(Workshop workshop, Long candidateId, Boolean completed) {
        this.workshop = workshop;
        this.candidateId = candidateId;
        this.completed = completed;
    }

}
