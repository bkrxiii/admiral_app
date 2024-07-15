package uk.ac.cardiff.phoenix.ilm.programs.service;

import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.programs.model.Homework;
import uk.ac.cardiff.phoenix.ilm.programs.model.Workshop;

import java.util.List;
import java.util.Set;

@Service
public interface HomeworkService {
    void createHomeworkForCandidateAndWorkshops(Long candidateId, Set<Workshop> workshops);

    Set<Homework> getHomeworksByCandidateID(Long candidateID);

    List<Homework> findAllHomeworksByCandidateId(Long candidateId);

    Homework setCompletedByHomeworkId(Long Id, boolean completed);

    Homework setCompleted(boolean completed);

    Homework findByHomeworkId(Long homeworkId);

    Homework findHomeworkById(Long homeworkId);

    Homework getHomeworkById(Long homeworkId);
}


