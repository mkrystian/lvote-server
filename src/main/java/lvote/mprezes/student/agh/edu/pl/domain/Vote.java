package lvote.mprezes.student.agh.edu.pl.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Vote.
 */
@Entity
@Table(name = "vote")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Vote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "voting_id")
    private Long votingId;

    @Column(name = "answer_id")
    private Long answerId;

    @Column(name = "random_number")
    private Long randomNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVotingId() {
        return votingId;
    }

    public Vote votingId(Long votingId) {
        this.votingId = votingId;
        return this;
    }

    public void setVotingId(Long votingId) {
        this.votingId = votingId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public Vote answerId(Long answerId) {
        this.answerId = answerId;
        return this;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Long getRandomNumber() {
        return randomNumber;
    }

    public Vote randomNumber(Long randomNumber) {
        this.randomNumber = randomNumber;
        return this;
    }

    public void setRandomNumber(Long randomNumber) {
        this.randomNumber = randomNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vote vote = (Vote) o;
        if (vote.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vote.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Vote{" +
            "id=" + getId() +
            ", votingId='" + getVotingId() + "'" +
            ", answerId='" + getAnswerId() + "'" +
            ", randomNumber='" + getRandomNumber() + "'" +
            "}";
    }
}
