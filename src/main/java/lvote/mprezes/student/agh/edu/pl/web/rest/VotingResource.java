package lvote.mprezes.student.agh.edu.pl.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import lvote.mprezes.student.agh.edu.pl.domain.User;
import lvote.mprezes.student.agh.edu.pl.domain.Voting;
import lvote.mprezes.student.agh.edu.pl.repository.VotingRepository;
import lvote.mprezes.student.agh.edu.pl.security.SecurityUtils;
import lvote.mprezes.student.agh.edu.pl.service.UserService;
import lvote.mprezes.student.agh.edu.pl.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Voting.
 */
@RestController
@RequestMapping("/api")
public class VotingResource {

    private final Logger log = LoggerFactory.getLogger(VotingResource.class);

    private static final String ENTITY_NAME = "voting";

    private final VotingRepository votingRepository;
    private final UserService userService;

    public VotingResource(VotingRepository votingRepository, UserService userService) {
        this.votingRepository = votingRepository;
        this.userService = userService;
    }

    /**
     * POST  /votings : Create a new voting.
     *
     * @param voting
     * 		the voting to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new voting, or with status 400 (Bad Request) if the voting has already an ID
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/votings")
    @Timed
    public ResponseEntity<Voting> createVoting(@Valid @RequestBody Voting voting) throws URISyntaxException {
        log.debug("REST request to save Voting : {}", voting);
        if (voting.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new voting cannot already have an ID")).body(null);
        }
        Voting result = votingRepository.save(voting);
        return ResponseEntity.created(new URI("/api/votings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /votings : Updates an existing voting.
     *
     * @param voting
     * 		the voting to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated voting,
     * or with status 400 (Bad Request) if the voting is not valid,
     * or with status 500 (Internal Server Error) if the voting couldnt be updated
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/votings")
    @Timed
    public ResponseEntity<Voting> updateVoting(@Valid @RequestBody Voting voting) throws URISyntaxException {
        log.debug("REST request to update Voting : {}", voting);
        if (voting.getId() == null) {
            return createVoting(voting);
        }
        Voting result = votingRepository.save(voting);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, voting.getId().toString()))
            .body(result);
    }

    /**
     * GET  /votings : get all the votings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of votings in body
     */
    @GetMapping("/votings")
    @Timed
    public List<Voting> getAllVotings() {
        log.debug("REST request to get all Votings");
        List<Voting> votings = votingRepository.findAllWithEagerRelationships();
        return votings;
    }

    /**
     * GET  /votings/:id : get the "id" voting.
     *
     * @param id
     * 		the id of the voting to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the voting, or with status 404 (Not Found)
     */
    @GetMapping("/votings/{id}")
    @Timed
    public ResponseEntity<Voting> getVoting(@PathVariable Long id) {
        log.debug("REST request to get Voting : {}", id);
        Voting voting = votingRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(voting));
    }

    /**
     * DELETE  /votings/:id : delete the "id" voting.
     *
     * @param id
     * 		the id of the voting to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/votings/{id}")
    @Timed
    public ResponseEntity<Void> deleteVoting(@PathVariable Long id) {
        log.debug("REST request to delete Voting : {}", id);
        votingRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /votings : get all the votings owned by user
     *
     * @return the ResponseEntity with status 200 (OK) and the list of votings in body
     */
    @GetMapping("/votings-owned")
    @Timed
    public List<Voting> getAllVotingsOwnedByUser() {
        log.debug("REST request to get all Votings owned by user");
        return votingRepository.findByOwnerIsCurrentUser();
    }

    /**
     * GET  /votings : get all the votings available for user ( by user group permission)
     *
     * @return the ResponseEntity with status 200 (OK) and the list of votings in body
     */
    @GetMapping("/votings-available")
    @Timed
    public List<Voting> getAllVotingsAvailableForUser() {
        log.debug("REST request to get all Votings available for user");
        return votingRepository.findAllByUserGroupContainingCurrentUser();
    }

    /**
     * Adds current user to list of already voted for given voting id
     *
     * @param votingId
     * 		Long representation of votingId as link to voting
     */
    void setUserAlreadyVoted(Long votingId) {
        Voting voting = votingRepository.findOneWithEagerRelationships(votingId);
        User currentUser = userService.getUserWithAuthorities();
        currentUser.setLogin(SecurityUtils.getCurrentUserLogin());
        voting.getAlreadyVoteds().add(currentUser);

        votingRepository.save(voting);
    }

    Optional<Voting> getOngoingVote(Long voteId) {
        return Optional.ofNullable(votingRepository.findByStartDateAfterAndEndDateBeforeAndIdEquals(voteId));
    }
}
