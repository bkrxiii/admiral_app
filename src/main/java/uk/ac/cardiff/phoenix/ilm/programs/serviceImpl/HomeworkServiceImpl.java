package uk.ac.cardiff.phoenix.ilm.programs.serviceImpl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.model.Event;
import uk.ac.cardiff.phoenix.ilm.programs.model.Homework;
import uk.ac.cardiff.phoenix.ilm.programs.repository.HomeworkRepository;
import uk.ac.cardiff.phoenix.ilm.programs.service.HomeworkService;

@Service
public class HomeworkServiceImpl implements HomeworkService {
    private final HomeworkRepository homeworkRepository;
    public HomeworkServiceImpl(HomeworkRepository homeworkRepository) {
        this.homeworkRepository = homeworkRepository;
    }

    @Override
    public void completeHomework(Event event, Candidate candidate) {
        // check if candidate is in event list
        // if yes, create homework object and save
        // if no, throw error
        Homework homework = homeworkRepository.findByEventAndCandidate(event, candidate);
        if (event.getApplicants().contains(candidate)) {
            homework.setCompleted(true);
            homeworkRepository.save(homework);
        } else {
            throw new DataIntegrityViolationException("Candidate is not in event list");
        }
    }

}
