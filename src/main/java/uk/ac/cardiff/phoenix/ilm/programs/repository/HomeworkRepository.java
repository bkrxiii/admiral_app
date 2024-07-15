package uk.ac.cardiff.phoenix.ilm.programs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.model.Event;
import uk.ac.cardiff.phoenix.ilm.programs.model.Homework;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long>{

    Homework findByEventAndCandidate(Event event, Candidate candidate);
}
