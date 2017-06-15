package lvote.mprezes.student.agh.edu.pl.domain;

import lvote.mprezes.student.agh.edu.pl.security.RSABlindSignaturesUtils.RSABlindedMessage;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * @author Krystian Majewski
 * @since 15.06.2017
 */

public class BlindedVote implements Serializable {

    private static final long serialVersionUID = 4844973479862789988L;

    private Long votingId;
    private RSABlindedMessage blindedMessage;

    public Long getVotingId() {
        return votingId;
    }

    public void setVotingId(Long votingId) {
        this.votingId = votingId;
    }

    public RSABlindedMessage getBlindedMessage() {
        return blindedMessage;
    }

    public void setBlindedMessage(RSABlindedMessage blindedMessage) {
        this.blindedMessage = blindedMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BlindedVote that = (BlindedVote) o;

        return new EqualsBuilder()
            .append(votingId, that.votingId)
            .append(blindedMessage, that.blindedMessage)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(votingId)
            .append(blindedMessage)
            .toHashCode();
    }
}
