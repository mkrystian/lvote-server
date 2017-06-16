package lvote.mprezes.student.agh.edu.pl.service;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static lvote.mprezes.student.agh.edu.pl.security.RSABlindSignaturesUtils.generateKeyPair;

/**
 * Service class for managing keys
 *
 * @author Krystian Majewski
 * @since 16.06.2017.
 */
@Service
@Transactional
public class KeysService {

    private static final HashMap<Long, AsymmetricCipherKeyPair> keysMap = new HashMap<>();

    /**
     * Returns key pair for specified voting, if not found create new
     *
     * @param votingId
     * 		Long id of voting
     *
     * @return AsymmetricCipherKeyPair for specified voting
     */
    public AsymmetricCipherKeyPair getKeyByVotingId(Long votingId) {
        if (!keysMap.containsKey(votingId)) {
            keysMap.put(votingId, generateKeyPair());
        }

        return keysMap.get(votingId);
    }
}
