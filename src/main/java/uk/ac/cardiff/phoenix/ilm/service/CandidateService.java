
// Ai was used as on influence

package uk.ac.cardiff.phoenix.ilm.service;

import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;

import java.time.LocalDate;
import java.util.List;

@Service
public interface CandidateService {

    // save created candidate into db
    Candidate saveCandidate(Candidate candidate);
    void saveCandidates(List<Candidate> candidates);

    // find candidate by email
    Candidate findCandidateByEmail(String email);

    Boolean existsCandidateByEmail(String email);

    Candidate findCandidateById(Long id);

    List<Candidate> findAll();

    List<Candidate> findCandidateByDepartment(String department);

    Candidate enrollCandidateInLevel(Candidate candidate, Level level);

    List<Candidate> findCandidatesWithRegistrationDateWithoutRegistrationNumber();

    List<Candidate> findCandidatesByLevelAndRegistrationDateBetween(Level level, LocalDate startDate, LocalDate endDate);

    List<Candidate> findUnregisteredCandidatesOverOneWeekOld();

    void registerILM(Candidate candidateById, Long ilmNumber);
}
