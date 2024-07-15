package uk.ac.cardiff.phoenix.ilm.programs.service;

import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.model.Event;

@Service
public interface HomeworkService {
    void completeHomework(Event event, Candidate candidate);
}
