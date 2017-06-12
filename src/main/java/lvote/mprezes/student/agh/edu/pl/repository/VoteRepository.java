package lvote.mprezes.student.agh.edu.pl.repository;

import lvote.mprezes.student.agh.edu.pl.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Vote entity.
 */
@SuppressWarnings("unused")
public interface VoteRepository extends JpaRepository<Vote,Long> {

}
