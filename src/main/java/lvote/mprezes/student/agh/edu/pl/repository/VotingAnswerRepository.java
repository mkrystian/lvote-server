package lvote.mprezes.student.agh.edu.pl.repository;

import lvote.mprezes.student.agh.edu.pl.domain.VotingAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the VotingAnswer entity.
 */
@SuppressWarnings("unused")
public interface VotingAnswerRepository extends JpaRepository<VotingAnswer, Long> {

}
