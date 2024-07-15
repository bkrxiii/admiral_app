
// Ai was used as on influence

package uk.ac.cardiff.phoenix.ilm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;

import java.time.LocalDate;
import java.util.List;

public interface CandidateRepo extends JpaRepository<Candidate, Long> {

    // searches go here
    Candidate findCandidateByEmail(String email);

    Candidate findCandidateById(Long id);

    List<Candidate> findCandidateByDepartment(String department);

    List<Candidate> findByRegistrationDateIsNotNullAndRegistrationNumberIsNull();

    List<Candidate> findByLevelAndRegistrationDateBetween(Level level, LocalDate startDate, LocalDate endDate);
    List<Candidate> findByRegistrationNumberIsNullAndRegistrationDateBefore(LocalDate cutoffDate);
    List<Candidate> findByRegistrationNumberIsNotNull();

}
