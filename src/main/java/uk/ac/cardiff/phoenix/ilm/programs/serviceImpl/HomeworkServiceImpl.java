package uk.ac.cardiff.phoenix.ilm.programs.serviceImpl;

import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.programs.model.Homework;
import uk.ac.cardiff.phoenix.ilm.programs.model.Workshop;
import uk.ac.cardiff.phoenix.ilm.programs.repository.HomeworkRepository;
import uk.ac.cardiff.phoenix.ilm.programs.repository.WorkshopsRepository;
import uk.ac.cardiff.phoenix.ilm.programs.service.HomeworkService;

import java.util.List;
import java.util.Set;

@Service
public class HomeworkServiceImpl implements HomeworkService {
    private HomeworkRepository homeworkRepository;
    private WorkshopsRepository workshopsRepository;

    @Override
    public void createHomeworkForCandidateAndWorkshops(Long candidateId, Set<Workshop> workshops) {
        // create homework object for each workshop
        for (Workshop workshop : workshops) {
            Homework homework = new Homework(workshop, candidateId, false);
            System.out.println("Homework object created: " + homework.toString()); // debug
            // save homework object
            homeworkRepository.save(homework);
        }
    }

    @Override
    public Set<Homework> getHomeworksByCandidateID(Long candidateID) {
        return null;
    }

    @Override
    public List<Homework> findAllHomeworksByCandidateId(Long candidateId) {
        return homeworkRepository.findAllHomeworksByCandidateId(candidateId);
    }

    @Override
    public Homework setCompletedByHomeworkId(Long Id, boolean completed) {
        return null;
    }

    @Override
    public Homework setCompleted(boolean completed) {
        return null;
    }

    @Override
    public Homework findByHomeworkId(Long homeworkId) {
        return null;
    }


    @Override
    public Homework findHomeworkById(Long homeworkId) {
        return homeworkRepository.findHomeworkById(homeworkId);
    }

    @Override
    public Homework getHomeworkById(Long homeworkId) {
        return homeworkRepository.getHomeworkById(homeworkId);
    }

}
