package lvote.mprezes.student.agh.edu.pl.repository;

import lvote.mprezes.student.agh.edu.pl.domain.Voting;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Voting entity.
 */
@SuppressWarnings("unused")
public interface VotingRepository extends JpaRepository<Voting,Long> {

    @Query("select voting from Voting voting where voting.owner.login = ?#{principal.username}")
    List<Voting> findByOwnerIsCurrentUser();

}
