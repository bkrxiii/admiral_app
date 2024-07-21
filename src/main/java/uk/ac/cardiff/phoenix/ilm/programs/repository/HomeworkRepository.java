package uk.ac.cardiff.phoenix.ilm.programs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.cardiff.phoenix.ilm.programs.model.Homework;
import uk.ac.cardiff.phoenix.ilm.programs.model.Workshop;

import java.util.List;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long>{

    Homework findByWorkshopAndCandidateId(Workshop workshop, Long candidateId);
    List<Homework> findAllHomeworksByCandidateId(Long candidateId);

    Homework findHomeworkById(Long homeworkId);

    Homework getHomeworkById(Long homeworkId);
}
