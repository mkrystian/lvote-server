package lvote.mprezes.student.agh.edu.pl.repository;

import lvote.mprezes.student.agh.edu.pl.domain.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Voting entity.
 */
@SuppressWarnings("unused")
public interface VotingRepository extends JpaRepository<Voting, Long> {

    @Query("select voting from Voting voting left join fetch voting.alreadyVoteds where voting.owner.login = ?#{principal.username}")
    List<Voting> findByOwnerIsCurrentUser();

    @Query("select distinct voting from Voting voting left join fetch voting.alreadyVoteds")
    List<Voting> findAllWithEagerRelationships();

    @Query("select voting from Voting voting left join fetch voting.alreadyVoteds where voting.id =:id")
    Voting findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select distinct voting from Voting voting left join fetch voting.content vc left join fetch vc.answers join fetch voting.userGroup ug join fetch ug.members mb left " +
        "join voting.alreadyVoteds av on av.login = ?#{principal.username} where mb.login = ?#{principal.username} and av.login is null ")
    List<Voting> findAllByUserGroupContainingCurrentUser();

}
