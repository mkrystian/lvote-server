package lvote.mprezes.student.agh.edu.pl.domain;

import lvote.mprezes.student.agh.edu.pl.security.RSABlindSignaturesUtils.RSAUnblindedSignature;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * @author Krystian Majewski
 * @since 15.06.2017
 */

public class UnblindedVote implements Serializable {

    private static final long serialVersionUID = 8098779496394734642L;

    private Long votingId;
    private Long answerId;
    private String randomNumber;
    private RSAUnblindedSignature signature;

    public Long getVotingId() {
        return votingId;
    }

    public void setVotingId(Long votingId) {
        this.votingId = votingId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public String getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(String randomNumber) {
        this.randomNumber = randomNumber;
    }

    public RSAUnblindedSignature getSignature() {
        return signature;
    }

    public void setSignature(RSAUnblindedSignature signature) {
        this.signature = signature;
    }

    public String getStringRepresentation() {
        return String.format("votingId=%s;answerId=%s;randomNumber=%s", votingId, answerId, randomNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UnblindedVote that = (UnblindedVote) o;

        return new EqualsBuilder()
            .append(votingId, that.votingId)
            .append(answerId, that.answerId)
            .append(randomNumber, that.randomNumber)
            .append(signature, that.signature)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(votingId)
            .append(answerId)
            .append(randomNumber)
            .append(signature)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("votingId", votingId)
            .append("answerId", answerId)
            .append("randomNumber", randomNumber)
            .append("signature", signature)
            .toString();
    }
}
