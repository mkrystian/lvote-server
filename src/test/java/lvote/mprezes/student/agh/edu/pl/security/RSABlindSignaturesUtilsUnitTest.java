package lvote.mprezes.student.agh.edu.pl.security;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.params.RSABlindingParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.junit.Ignore;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test suit for {@link RSABlindSignaturesUtils}
 *
 * @author Krystian Majewski
 * @since 14.06.2017.
 */
public class RSABlindSignaturesUtilsUnitTest {

    private static final String ENCODING_UTF8 = "UTF8";

    @Test
    public void testGenerateKeyPair() {
        AsymmetricCipherKeyPair keyPair1 = RSABlindSignaturesUtils.generateKeyPair();
        AsymmetricCipherKeyPair keyPair2 = RSABlindSignaturesUtils.generateKeyPair();

        assertThat(keyPair1).isNotNull();
        assertThat(keyPair1.getPublic()).isNotNull();
        assertThat(keyPair1.getPrivate()).isNotNull();
        assertThat(keyPair2).isNotNull();
        assertThat(keyPair2.getPublic()).isNotNull();
        assertThat(keyPair2.getPrivate()).isNotNull();
        assertThat(keyPair1).isNotEqualTo(keyPair2);
    }

    @Test
    public void testBlindMessage() throws CryptoException, UnsupportedEncodingException {
        /*AsymmetricCipherKeyPair keyPair = RSABlindSignaturesUtils.generateKeyPair();
        String message = "some message";

        byte[] blindedMessage = RSABlindSignaturesUtils.blindMessage(message, );

        assertThat(blindedMessage).isNotNull().isNotEmpty().isNotEqualTo(message.getBytes(ENCODING_UTF8));*/
    }

    @Test
    public void testSignMessage() throws UnsupportedEncodingException {
        AsymmetricCipherKeyPair keyPair = RSABlindSignaturesUtils.generateKeyPair();
        byte[] message = "some message".getBytes(ENCODING_UTF8);

        byte[] signature = RSABlindSignaturesUtils.signMessage(message, (RSAKeyParameters) keyPair.getPrivate());

        assertThat(signature).isNotNull().isNotEmpty().isNotEqualTo(message);
    }

    @Test
    @Ignore
    public void testVerifySignature() throws UnsupportedEncodingException {
        AsymmetricCipherKeyPair keyPair = RSABlindSignaturesUtils.generateKeyPair();
        byte[] message = "some message".getBytes(ENCODING_UTF8);
        byte[] signature = RSABlindSignaturesUtils.signMessage(message, (RSAKeyParameters) keyPair.getPrivate());

        assertThat(RSABlindSignaturesUtils.verifySignature(message, signature, keyPair.getPublic())).isTrue();
    }

    @Test
    public void testFullPath() throws CryptoException, UnsupportedEncodingException {
        String someMessage = "some text";
        AsymmetricCipherKeyPair signerKeyPair = RSABlindSignaturesUtils.generateKeyPair();
        RSABlindingParameters rsaBlindingParameters = RSABlindSignaturesUtils.generateRSABlindingParameters((RSAKeyParameters) signerKeyPair.getPublic());

        byte[] blindedMessage = RSABlindSignaturesUtils.blindMessage(someMessage, rsaBlindingParameters);
        byte[] signature = RSABlindSignaturesUtils.signMessage(blindedMessage, (RSAKeyParameters) signerKeyPair.getPrivate());
        byte[] unblindedMessage = RSABlindSignaturesUtils.unblindMessage(signature, rsaBlindingParameters);
        assertThat(RSABlindSignaturesUtils.verifySignature(someMessage.getBytes(ENCODING_UTF8), unblindedMessage, signerKeyPair.getPublic())).isTrue();


    }
}
