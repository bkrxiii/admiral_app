package uk.ac.cardiff.phoenix.ilm.service;

import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public interface CandidateReportService {

    public void reportUnregisteredCandidates();

    List<Candidate> reportUnregisteredCandidatesApproachingDeadline();

    void reportRegisteredCandidates() throws IOException;


    public void reportRegisteredCandidates(Level level, LocalDate start, LocalDate end) throws IOException;




}
