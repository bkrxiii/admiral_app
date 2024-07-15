
// Ai was used as on influence

package uk.ac.cardiff.phoenix.ilm.serviceImpl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import uk.ac.cardiff.phoenix.ilm.repository.CandidateRepo;
import uk.ac.cardiff.phoenix.ilm.service.CandidateService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    Logger logger = LoggerFactory.getLogger(CandidateServiceImpl.class);
    private final CandidateRepo candidateRepo;

    @Override
    public Candidate saveCandidate(Candidate candidate) {
        if (candidateRepo.findCandidateByEmail(candidate.getEmail()) != null) {
            throw new EntityExistsException("Candidate with email " + candidate.getEmail() + " already exists");
        } else if (candidateRepo.findCandidateById(candidate.getId()) != null) {
            throw new EntityExistsException("Candidate with id " + candidate.getId() + " already exists");
        } else {
                logger.info("New Candidate SAVED. Email: " + candidate.getEmail());
                return candidateRepo.save(candidate);
            }
    }

    @Override
    public void saveCandidates(List<Candidate> candidates) {
        logger.info("Candidates' list SAVED.");
        candidateRepo.saveAll(candidates); // needed to actually save the candidates...
    }

    @Override
    public Candidate findCandidateByEmail(String email) {
        if (candidateRepo.findCandidateByEmail(email) == null) {
            logger.info("Candidate search UNSUCCESSFUL. Email: " + email);
            throw new EntityNotFoundException("Candidate with email " + email + " could not be found.");
        } else {
            logger.info("Candidate search SUCCESS. Email: " + email);
            return candidateRepo.findCandidateByEmail(email);
        }
    }

    @Override
    public Boolean existsCandidateByEmail(String email) {
        logger.info("Candidate exists check. Email: " + email);
        return candidateRepo.findCandidateByEmail(email) != null;
    }

    @Override
    public Candidate findCandidateById(Long id) {
        if (candidateRepo.findCandidateById(id) == null) {
            logger.info("Candidate search UNSUCCESSFUL. ID: " + id);
            throw new EntityNotFoundException("Candidate with id " + id + " could not be found.");
        } else {
            logger.info("Candidate search SUCCESS. ID: " + id);
            return candidateRepo.findCandidateById(id);
        }
    }

    @Override
    public List<Candidate> findAll() {
        logger.info("Candidates' list findAll().");
        return candidateRepo.findAll();
    }

    @Override
    public List<Candidate> findCandidateByDepartment(String department) {
        logger.info("Candidate search SUCCESS. Department: " + department);
        return candidateRepo.findCandidateByDepartment(department);
    }

    @Override
    public Candidate enrollCandidateInLevel(Candidate candidate, Level level) {
        if (candidate.getLevel() != null) {
            logger.info("Candidate level enrollment UNSUCCESSFUL. Candidate: " + candidate + " Level: " + level);
            throw new EntityExistsException("Candidate is already enrolled in a level.");
        } else {
            candidate.setLevel(level);
            logger.info("Candidate level enrollment SUCCESS. Candidate: " + candidate + " Level: " + level);
            return candidateRepo.save(candidate);
        }
    }

    @Override
    public List<Candidate> findCandidatesWithRegistrationDateWithoutRegistrationNumber() {
        return candidateRepo.findByRegistrationDateIsNotNullAndRegistrationNumberIsNull();
    }

    @Override
    public List<Candidate> findCandidatesByLevelAndRegistrationDateBetween(Level level, LocalDate startDate, LocalDate endDate) {
        return candidateRepo.findByLevelAndRegistrationDateBetween(level, startDate, endDate);
    }

    @Override
    public List<Candidate> findUnregisteredCandidatesOverOneWeekOld() {
        LocalDate cutoffDate = LocalDate.now().minusWeeks(1);
        return candidateRepo.findByRegistrationNumberIsNullAndRegistrationDateBefore(cutoffDate);
    }

    @Override
    public void registerILM(Candidate candidateById, Long ilmNumber) {
        if (candidateById.getRegistrationNumber() != null) {
            throw new EntityExistsException("Candidate already has a registration number.");
        }
        candidateById.setRegistrationNumber(ilmNumber);
        candidateRepo.save(candidateById);
    }

    @Override
    public void enrollCandidate(Object any, long anyLong) {

    }

}
