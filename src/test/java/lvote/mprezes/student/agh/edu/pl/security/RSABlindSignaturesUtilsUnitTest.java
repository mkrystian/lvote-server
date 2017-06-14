package lvote.mprezes.student.agh.edu.pl.security;

import lvote.mprezes.student.agh.edu.pl.security.RSABlindSignaturesUtils.*;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.params.RSABlindingParameters;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static lvote.mprezes.student.agh.edu.pl.security.RSABlindSignaturesUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test suit for {@link RSABlindSignaturesUtils}
 *
 * @author Krystian Majewski
 * @since 14.06.2017.
 */
public class RSABlindSignaturesUtilsUnitTest {

    private static final String MESSAGE = "some text to be sent";
    private final AsymmetricCipherKeyPair signerKeyPair = generateKeyPair();
    private final RSABlindingParameters rsaBlindingParameters = generateRSABlindingParameters(signerKeyPair.getPublic());
    private final RSABlindingParameters rsaBlindingParameters2 = generateRSABlindingParameters(signerKeyPair.getPublic());
    private final AsymmetricCipherKeyPair signerKeyPair2 = generateKeyPair();

    @Test
    public void testGenerateKeyPair() {
        AsymmetricCipherKeyPair keyPair1 = generateKeyPair();
        AsymmetricCipherKeyPair keyPair2 = generateKeyPair();

        assertThat(keyPair1).isNotNull();
        assertThat(keyPair1.getPublic()).isNotNull();
        assertThat(keyPair1.getPrivate()).isNotNull();
        assertThat(keyPair2).isNotNull();
        assertThat(keyPair2.getPublic()).isNotNull();
        assertThat(keyPair2.getPrivate()).isNotNull();
        assertThat(keyPair1).isNotEqualTo(keyPair2);
    }

    @Test
    public void testGenerateRSABlindingParameters() {
        RSABlindingParameters rsaBlindingParameters = generateRSABlindingParameters(signerKeyPair.getPublic());
        RSABlindingParameters rsaBlindingParameters2 = generateRSABlindingParameters(signerKeyPair2.getPublic());

        assertThat(rsaBlindingParameters).isNotNull().isNotEqualTo(rsaBlindingParameters2);
    }

    @Test
    public void testBlindMessage() throws CryptoException, UnsupportedEncodingException {
        RSABlindedMessage rsaBlindedMessage = blindMessage(MESSAGE, rsaBlindingParameters);
        RSABlindedMessage rsaBlindedMessage2 = blindMessage(MESSAGE, rsaBlindingParameters2);
        RSABlindedMessage rsaBlindedMessage3 = blindMessage("other text", rsaBlindingParameters2);

        assertThat(rsaBlindedMessage).isNotNull().isNotEqualTo(rsaBlindedMessage2).isNotEqualTo(rsaBlindedMessage3);
        assertThat(rsaBlindedMessage2).isNotNull().isNotEqualTo(rsaBlindedMessage3);
        assertThat(rsaBlindedMessage3).isNotNull();

    }

    @Test
    public void testSignMessage() throws UnsupportedEncodingException {
        RSABlindedMessage rsaBlindedMessage = new RSABlindedMessage();
        rsaBlindedMessage.setContent(MESSAGE.getBytes());
        RSABlindedSignature rsaBlindedSignature = signMessage(rsaBlindedMessage, signerKeyPair.getPrivate());

        assertThat(rsaBlindedSignature).isNotNull();
    }

    @Test
    public void testVerifySignature() throws UnsupportedEncodingException {
        RSAUnblindedSignature rsaUnblindedSignature = new RSAUnblindedSignature();
        rsaUnblindedSignature.setContent(MESSAGE.getBytes());
        rsaUnblindedSignature.setOriginalMessage(MESSAGE);

        assertThat(verifySignature(rsaUnblindedSignature, signerKeyPair.getPublic())).isFalse();
    }

    @Test
    public void testUnblindUnblindSignature() {
        RSABlindedSignature rsaBlindedMessage = new RSABlindedSignature();
        rsaBlindedMessage.setContent(MESSAGE.getBytes());

        RSAUnblindedSignature rsaUnblindedSignature = unblindSignature(rsaBlindedMessage, MESSAGE, rsaBlindingParameters);

        assertThat(rsaUnblindedSignature).isNotNull();
    }

    @Test
    public void testFullPathCorrect() throws CryptoException, UnsupportedEncodingException {

        RSABlindedMessage rsaBlindedMessage = blindMessage(MESSAGE, rsaBlindingParameters);
        RSABlindedSignature rsaBlindedSignature = signMessage(rsaBlindedMessage, signerKeyPair.getPrivate());
        RSAUnblindedSignature rsaUnblindedSignature = unblindSignature(rsaBlindedSignature, MESSAGE, rsaBlindingParameters);
        boolean result = verifySignature(rsaUnblindedSignature, signerKeyPair.getPublic());

        assertThat(result).isTrue();
    }

    @Test
    public void testFullPathFailsIncorrectMessage() throws CryptoException {

        RSABlindedMessage rsaBlindedMessage = blindMessage(MESSAGE, rsaBlindingParameters);
        RSABlindedSignature rsaBlindedSignature = signMessage(rsaBlindedMessage, signerKeyPair.getPrivate());
        RSAUnblindedSignature rsaUnblindedSignature = unblindSignature(rsaBlindedSignature, "other message", rsaBlindingParameters);
        boolean result = verifySignature(rsaUnblindedSignature, signerKeyPair.getPublic());

        assertThat(result).isFalse();
    }

    @Test
    public void testFullPathFailsIncorrectBlindingParameters() throws CryptoException, UnsupportedEncodingException {

        RSABlindedMessage rsaBlindedMessage = blindMessage(MESSAGE, rsaBlindingParameters);
        RSABlindedSignature rsaBlindedSignature = signMessage(rsaBlindedMessage, signerKeyPair.getPrivate());
        RSAUnblindedSignature rsaUnblindedSignature = unblindSignature(rsaBlindedSignature, MESSAGE, generateRSABlindingParameters(signerKeyPair.getPublic()));
        boolean result = verifySignature(rsaUnblindedSignature, signerKeyPair.getPublic());

        assertThat(result).isFalse();
    }

    @Test
    public void testFullPathFailsIncorrectPrivateKey() throws CryptoException {

        RSABlindedMessage rsaBlindedMessage = blindMessage(MESSAGE, rsaBlindingParameters);
        RSABlindedSignature rsaBlindedSignature = signMessage(rsaBlindedMessage, generateKeyPair().getPrivate());
        RSAUnblindedSignature rsaUnblindedSignature = unblindSignature(rsaBlindedSignature, MESSAGE, rsaBlindingParameters);
        boolean result = verifySignature(rsaUnblindedSignature, signerKeyPair.getPublic());

        assertThat(result).isFalse();
    }

    @Test
    public void testFullPathFailIncorrectPublicKey() throws CryptoException, UnsupportedEncodingException {

        RSABlindedMessage rsaBlindedMessage = blindMessage(MESSAGE, rsaBlindingParameters);
        RSABlindedSignature rsaBlindedSignature = signMessage(rsaBlindedMessage, signerKeyPair.getPrivate());
        RSAUnblindedSignature rsaUnblindedSignature = unblindSignature(rsaBlindedSignature, MESSAGE, rsaBlindingParameters);
        boolean result = verifySignature(rsaUnblindedSignature, signerKeyPair2.getPublic());

        assertThat(result).isFalse();
    }

}
