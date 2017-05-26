package lvote.mprezes.student.agh.edu.pl.repository;

import lvote.mprezes.student.agh.edu.pl.domain.VotingContent;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the VotingContent entity.
 */
@SuppressWarnings("unused")
public interface VotingContentRepository extends JpaRepository<VotingContent,Long> {

}
