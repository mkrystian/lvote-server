package lvote.mprezes.student.agh.edu.pl.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import lvote.mprezes.student.agh.edu.pl.domain.BlindedVote;
import lvote.mprezes.student.agh.edu.pl.domain.SignedVote;
import lvote.mprezes.student.agh.edu.pl.domain.UnblindedVote;
import lvote.mprezes.student.agh.edu.pl.domain.Vote;
import lvote.mprezes.student.agh.edu.pl.repository.VoteRepository;
import lvote.mprezes.student.agh.edu.pl.security.RSABlindSignaturesUtils;
import lvote.mprezes.student.agh.edu.pl.web.rest.util.HeaderUtil;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing Vote.
 */
@RestController
@RequestMapping("/api")
public class VoteResource {

    private final static AsymmetricCipherKeyPair keyPair = RSABlindSignaturesUtils.generateKeyPair();

    private final Logger log = LoggerFactory.getLogger(VoteResource.class);

    private static final String ENTITY_NAME = "vote";
    private final VoteRepository voteRepository;
    private final VotingResource votingResource;

    public VoteResource(VoteRepository voteRepository, VotingResource votingResource) {
        this.voteRepository = voteRepository;
        this.votingResource = votingResource;
    }

    /**
     * POST  /votes : Create a new vote.
     *
     * @param vote
     * 		the vote to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new vote, or with status 400 (Bad Request) if the vote has already an ID
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/votes")
    @Timed
    public ResponseEntity<Vote> createVote(@RequestBody Vote vote) throws URISyntaxException {
        log.debug("REST request to save Vote : {}", vote);
        if (vote.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new vote cannot already have an ID")).body(null);
        }
        Vote result = voteRepository.save(vote);
        return ResponseEntity.created(new URI("/api/votes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /votes : Updates an existing vote.
     *
     * @param vote
     * 		the vote to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated vote,
     * or with status 400 (Bad Request) if the vote is not valid,
     * or with status 500 (Internal Server Error) if the vote couldnt be updated
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/votes")
    @Timed
    public ResponseEntity<Vote> updateVote(@RequestBody Vote vote) throws URISyntaxException {
        log.debug("REST request to update Vote : {}", vote);
        if (vote.getId() == null) {
            return createVote(vote);
        }
        Vote result = voteRepository.save(vote);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vote.getId().toString()))
            .body(result);
    }

    /**
     * GET  /votes : get all the votes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of votes in body
     */
    @GetMapping("/votes")
    @Timed
    public List<Vote> getAllVotes() {
        log.debug("REST request to get all Votes");
        List<Vote> votes = voteRepository.findAll();
        return votes;
    }

    /**
     * GET  /votes/:id : get the "id" vote.
     *
     * @param id
     * 		the id of the vote to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the vote, or with status 404 (Not Found)
     */
    @GetMapping("/votes/{id}")
    @Timed
    public ResponseEntity<Vote> getVote(@PathVariable Long id) {
        log.debug("REST request to get Vote : {}", id);
        Vote vote = voteRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(vote));
    }

    /**
     * DELETE  /votes/:id : delete the "id" vote.
     *
     * @param id
     * 		the id of the vote to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/votes/{id}")
    @Timed
    public ResponseEntity<Void> deleteVote(@PathVariable Long id) {
        log.debug("REST request to delete Vote : {}", id);
        voteRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PutMapping("vote/sign")
    @Timed
    public ResponseEntity signVote(@RequestBody BlindedVote blindedVote) {
        log.debug("REST request to sign Vote for Voting id : {}", blindedVote.getVotingId());
        if (!validInputSignVote(blindedVote.getVotingId())) {
            return new ResponseEntity<>("User could not vote in this voting", HttpStatus.FORBIDDEN);
        }
        SignedVote result = new SignedVote();
        result.setBlindedSignature(RSABlindSignaturesUtils.signMessage(blindedVote.getBlindedMessage(), PublicKeyResource.keyPair.getPrivate()));

        votingResource.setUserAlreadyVoted(blindedVote.getVotingId());

        return ResponseEntity.ok(result);
    }

    private boolean validInputSignVote(Long votingId) {
        return votingResource.getAllVotingsAvailableForUser().stream().anyMatch(voting -> Objects.equals(voting.getId(), votingId));
    }

    @PostMapping("vote/unblinded")
    @Timed
    public ResponseEntity<Boolean> putVoteSigned(@RequestBody UnblindedVote unblindedVote) {
        log.debug("REST request to add signed vote, Voting id : {}, Answer id : {}, Random number : {}", unblindedVote.getVotingId(), unblindedVote.getAnswerId(), unblindedVote.getRandomNumber());

        if (signatureValidation(unblindedVote) && checkIfNotExists(unblindedVote)) {
            log.debug("Vote verification OK.");
            addVote(unblindedVote);
            return ResponseEntity.ok(true);
        } else {
            log.debug("Vote verification failed - vote nod added!");
            return ResponseEntity.ok(false);
        }
    }

    private void addVote(UnblindedVote unblindedVote) {
        Vote vote = new Vote()
            .answerId(unblindedVote.getAnswerId())
            .votingId(unblindedVote.getVotingId())
            .randomNumber(unblindedVote.getRandomNumber().toString());

        voteRepository.save(vote);
    }

    private boolean checkIfNotExists(@RequestBody UnblindedVote unblindedVote) {

        return voteRepository.findAllByAnswerIdAnAndVotingIdAndRandomNumber(unblindedVote.getVotingId(), unblindedVote.getRandomNumber().toString()).isEmpty();
    }


    private boolean signatureValidation(UnblindedVote unblindedVote) {
        String originalMessage = unblindedVote.getStringRepresentation();
        return RSABlindSignaturesUtils.verifySignature(unblindedVote.getSignature(), originalMessage, PublicKeyResource.keyPair.getPublic());
    }

}
