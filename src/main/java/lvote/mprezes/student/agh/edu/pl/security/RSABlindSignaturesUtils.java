package lvote.mprezes.student.agh.edu.pl.security;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.engines.RSABlindingEngine;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.generators.RSABlindingFactorGenerator;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSABlindingParameters;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.signers.PSSSigner;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Krystian Majewski
 * @since 14.06.2017.
 */
public class RSABlindSignaturesUtils {

    private static final String ENCODING_UTF8 = "UTF8";

    private RSABlindSignaturesUtils() {

    }

    /**
     * Generate 2048-bit RSA key pair of public and private key.
     * Due to usage of SecureRandom pair is generated randomly and could not be predicted.
     *
     * @return AsymmetricCipherKeyPair representation of key pair
     */
    public static AsymmetricCipherKeyPair generateKeyPair() {
        // Generate a 2048-bit RSA key pair.
        RSAKeyPairGenerator generator = new RSAKeyPairGenerator();
        generator.init(new RSAKeyGenerationParameters(
            new BigInteger("10001", 16), new SecureRandom(), 2048,
            80));
        return generator.generateKeyPair();
    }

    /**
     * Generate blinding parameters for blinding message.
     *
     * @param publicKey
     * 		signer public key
     *
     * @return RSABlindingParameters
     */
    public static RSABlindingParameters generateRSABlindingParameters(@NotNull AsymmetricKeyParameter publicKey) {
        RSABlindingFactorGenerator blindingFactorGenerator
            = new RSABlindingFactorGenerator();
        blindingFactorGenerator.init(publicKey);

        BigInteger blindingFactor
            = blindingFactorGenerator.generateBlindingFactor();

        return new RSABlindingParameters((RSAKeyParameters) publicKey, blindingFactor);
    }

    /**
     * Blinds message passed as String using blinding parameters.
     *
     * @param message
     * 		String message to blind
     * @param blindingParameters
     * 		parameters needed to blind message
     *
     * @return RSABlindedMessage
     * @throws CryptoException
     * 		in case of any blinding problems
     */
    public static RSABlindedMessage blindMessage(@NotNull String message, @NotNull RSABlindingParameters blindingParameters) throws CryptoException {
        RSABlindedMessage result = new RSABlindedMessage();

        byte[] byteMessageRepresentation = stringToBytes(message);

        PSSSigner signer = new PSSSigner(new RSABlindingEngine(),
            new SHA1Digest(), 20);
        signer.init(true, blindingParameters);

        signer.update(byteMessageRepresentation, 0, byteMessageRepresentation.length);

        result.content = signer.generateSignature();

        return result;
    }

    /**
     * Returns unblinded signature with original message
     *
     * @param blindedSignature
     * 		RSABlindedSignature
     * @param blindingParameters
     * 		RSABlindingParameters
     *
     * @return Object containing unblinded signature and original message
     */
    public static RSAUnblindedSignature unblindSignature(@NotNull RSABlindedSignature blindedSignature, @NotNull RSABlindingParameters blindingParameters) {
        RSAUnblindedSignature result = new RSAUnblindedSignature();

        RSABlindingEngine blindingEngine = new RSABlindingEngine();
        blindingEngine.init(false, blindingParameters);

        result.content = blindingEngine.processBlock(blindedSignature.content, 0, blindedSignature.content.length);

        return result;
    }

    /**
     * Signs blinded message with signer private key
     *
     * @param blindedMessage
     * 		RSABlindedMessage
     * @param privateKey
     * 		signer private RSA key
     *
     * @return RSABlindedSignature
     */
    public static RSABlindedSignature signMessage(RSABlindedMessage blindedMessage, AsymmetricKeyParameter privateKey) {
        RSABlindedSignature result = new RSABlindedSignature();

        RSAEngine engine = new RSAEngine();
        engine.init(true, privateKey);

        result.content = engine.processBlock(blindedMessage.content, 0, blindedMessage.content.length);

        return result;
    }

    /**
     * Verify if passed message is compatible with signature using signer public key
     *
     * @param unblindedSignature
     * 		UnblindedSignature
     * @param originalMessage
     * 		String representation of original message
     * @param publicKey
     * 		singer RSA public key
     *
     * @return true if verification correct, false otherwise
     */
    public static boolean verifySignature(RSAUnblindedSignature unblindedSignature, String originalMessage, AsymmetricKeyParameter publicKey) {
        byte[] originalMessageByte = stringToBytes(originalMessage);
        PSSSigner signer = new PSSSigner(new RSAEngine(), new SHA1Digest(), 20);
        signer.init(false, publicKey);
        signer.update(originalMessageByte, 0, originalMessageByte.length);

        return signer.verifySignature(unblindedSignature.content);
    }

    private static byte[] stringToBytes(@NotNull String text) {
        try {
            return text.getBytes(ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Could not find encoding: " + ENCODING_UTF8, e);
        }
    }

    private static String bytesToString(@NotNull byte[] text) {
        try {
            return new String(text, ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Could not find encoding: " + ENCODING_UTF8, e);
        }
    }

    public static class RSABlindedMessage implements Serializable {
        private static final long serialVersionUID = 4669890053466414694L;

        private byte[] content;

        public byte[] getContent() {
            return content;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (!(o instanceof RSABlindedMessage)) return false;

            RSABlindedMessage that = (RSABlindedMessage) o;

            return new EqualsBuilder()
                .append(content, that.content)
                .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                .append(content)
                .toHashCode();
        }
    }

    public static class RSABlindedSignature implements Serializable {

        private static final long serialVersionUID = -3184098262998759568L;

        byte[] content;

        public byte[] getContent() {
            return content;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (!(o instanceof RSABlindedSignature)) return false;

            RSABlindedSignature that = (RSABlindedSignature) o;

            return new EqualsBuilder()
                .append(content, that.content)
                .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                .append(content)
                .toHashCode();
        }
    }

    public static class RSAUnblindedSignature implements Serializable {
        private static final long serialVersionUID = 6336896071726519590L;

        private byte[] content;

        public byte[] getContent() {
            return content;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (!(o instanceof RSAUnblindedSignature)) return false;

            RSAUnblindedSignature that = (RSAUnblindedSignature) o;

            return new EqualsBuilder()
                .append(content, that.content)
                .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                .append(content)
                .toHashCode();
        }
    }
}
