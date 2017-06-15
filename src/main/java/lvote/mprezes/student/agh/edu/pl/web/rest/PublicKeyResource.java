package lvote.mprezes.student.agh.edu.pl.web.rest;

import com.codahale.metrics.annotation.Timed;
import lvote.mprezes.student.agh.edu.pl.security.RSABlindSignaturesUtils;
import lvote.mprezes.student.agh.edu.pl.service.dto.RSAKeyParametersDTO;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing the server public key.
 */
@RestController
@RequestMapping("/api")
public class PublicKeyResource {

    static final AsymmetricCipherKeyPair keyPair = RSABlindSignaturesUtils.generateKeyPair();

    private final Logger log = LoggerFactory.getLogger(PublicKeyResource.class);


    @GetMapping("/key/public")
    @Timed
    public ResponseEntity<RSAKeyParametersDTO> getPublicKey() {
        log.debug("REST request get public key");
        RSAKeyParametersDTO result = new RSAKeyParametersDTO();
        RSAKeyParameters publicKey = ((RSAKeyParameters) keyPair.getPublic());

        result.setExponent(publicKey.getExponent());
        result.setModulus(publicKey.getModulus().toString());
        result.setPrivate(publicKey.isPrivate());

        return ResponseEntity.ok(result);
    }
}
