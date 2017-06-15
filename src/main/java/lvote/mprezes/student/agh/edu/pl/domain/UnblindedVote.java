package lvote.mprezes.student.agh.edu.pl.domain;

import lvote.mprezes.student.agh.edu.pl.security.RSABlindSignaturesUtils.RSAUnblindedSignature;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * @author Krystian Majewski
 * @since 15.06.2017
 */

public class UnblindedVote implements Serializable {

    private static final long serialVersionUID = 8098779496394734642L;

    private Long votingId;
    private Long answerId;
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

    public RSAUnblindedSignature getSignature() {
        return signature;
    }

    public void setSignature(RSAUnblindedSignature signature) {
        this.signature = signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof UnblindedVote)) return false;

        UnblindedVote that = (UnblindedVote) o;

        return new EqualsBuilder()
            .append(votingId, that.votingId)
            .append(answerId, that.answerId)
            .append(signature, that.signature)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(votingId)
            .append(answerId)
            .append(signature)
            .toHashCode();
    }
}
