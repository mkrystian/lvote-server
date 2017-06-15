package lvote.mprezes.student.agh.edu.pl.repository;

import lvote.mprezes.student.agh.edu.pl.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Vote entity.
 */
@SuppressWarnings("unused")
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("select vote from Vote vote where vote.votingId = :votingId and vote.randomNumber = :randomNumber")
    List<Vote> findAllByAnswerIdAnAndVotingIdAndRandomNumber(@Param("votingId") Long votingId, @Param("randomNumber") String randomNumber);

}
