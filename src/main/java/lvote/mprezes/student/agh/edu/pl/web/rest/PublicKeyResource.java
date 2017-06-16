package lvote.mprezes.student.agh.edu.pl.web.rest;

import com.codahale.metrics.annotation.Timed;
import lvote.mprezes.student.agh.edu.pl.service.KeysService;
import lvote.mprezes.student.agh.edu.pl.service.dto.RSAKeyParametersDTO;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing the server public key.
 */
@RestController
@RequestMapping("/api")
public class PublicKeyResource {

    private final Logger log = LoggerFactory.getLogger(PublicKeyResource.class);

    private final KeysService keysService;

    public PublicKeyResource(KeysService keysService) {
        this.keysService = keysService;
    }


    @GetMapping("/key/public/{votingId}")
    @Timed
    public ResponseEntity<RSAKeyParametersDTO> getPublicKey(@PathVariable("votingId") Long votingId) {
        log.debug("REST request get public key");
        RSAKeyParametersDTO result = new RSAKeyParametersDTO();
        RSAKeyParameters publicKey = ((RSAKeyParameters) keysService.getKeyByVotingId(votingId).getPublic());

        result.setExponent(publicKey.getExponent());
        result.setModulus(publicKey.getModulus().toString());
        result.setPrivate(publicKey.isPrivate());

        return ResponseEntity.ok(result);
    }
}
