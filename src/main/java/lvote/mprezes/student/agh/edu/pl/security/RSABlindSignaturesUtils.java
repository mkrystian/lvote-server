package lvote.mprezes.student.agh.edu.pl.security;

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
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Krystian Majewski
 * @since 14.06.2017.
 */
public class RSABlindSignaturesUtils {

    private static final String ENCODING_UTF8 = "UTF8";

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

    public static RSABlindingParameters generateRSABlindingParameters(@NotNull RSAKeyParameters publicKey) {
        //Create random factor generator
        RSABlindingFactorGenerator blindingFactorGenerator
            = new RSABlindingFactorGenerator();
        blindingFactorGenerator.init(publicKey);

        //Generate random factor
        BigInteger blindingFactor
            = blindingFactorGenerator.generateBlindingFactor();

        // Generate blinding parameters
        return new RSABlindingParameters(publicKey, blindingFactor);
    }

    /**
     * Blinds message passed as using blinding parameters.
     *
     * @param message
     * 		String message to blind
     * @param blindingParameters
     * 		parameters needed to blind message
     *
     * @return byte[] representation of blinded message
     * @throws CryptoException
     * 		in case of any blinding problems
     */
    public static byte[] blindMessage(@NotNull String message, @NotNull RSABlindingParameters blindingParameters) throws CryptoException {
        byte[] byteMessageRepresentation = stringToBytes(message);

        PSSSigner signer = new PSSSigner(new RSABlindingEngine(),
            new SHA1Digest(), 20);
        signer.init(true, blindingParameters);

        // Blind message
        signer.update(byteMessageRepresentation, 0, byteMessageRepresentation.length);

        return signer.generateSignature();
    }

    public static byte[] unblindMessage(@NotNull byte[] signature, @NotNull RSABlindingParameters blindingParameters) {
        // "Unblind" the bank's signature (so to speak) and create a new coin
        // using the ID and the unblinded signature.
        RSABlindingEngine blindingEngine = new RSABlindingEngine();
        blindingEngine.init(false, blindingParameters);

        return blindingEngine.processBlock(signature, 0, signature.length);
    }

    public static byte[] signMessage(byte[] message, RSAKeyParameters privateKey) {
        RSAEngine engine = new RSAEngine();
        engine.init(true, privateKey);

        return engine.processBlock(message, 0, message.length);
    }

    public static boolean verifySignature(byte[] message, byte[] signature, AsymmetricKeyParameter publicKey) {
        PSSSigner signer = new PSSSigner(new RSAEngine(), new SHA1Digest(), 20);
        signer.init(false, publicKey);
        signer.update(message, 0, message.length);

        return signer.verifySignature(signature);
    }

    private static byte[] stringToBytes(@NotNull String message) {
        try {
            return message.getBytes(ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Could not find encoding: " + ENCODING_UTF8, e);
        }
    }
}
