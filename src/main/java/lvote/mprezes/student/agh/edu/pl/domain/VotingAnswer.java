package lvote.mprezes.student.agh.edu.pl.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A VotingAnswer.
 */
@Entity
@Table(name = "voting_answer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VotingAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "answer", nullable = false)
    private String answer;

    @ManyToOne
    @JsonIgnore
    private VotingContent votingContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public VotingAnswer answer(String answer) {
        this.answer = answer;
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public VotingContent getVotingContent() {
        return votingContent;
    }

    public VotingAnswer votingContent(VotingContent votingContent) {
        this.votingContent = votingContent;
        return this;
    }

    public void setVotingContent(VotingContent votingContent) {
        this.votingContent = votingContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VotingAnswer votingAnswer = (VotingAnswer) o;
        if (votingAnswer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), votingAnswer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VotingAnswer{" +
            "id=" + getId() +
            ", answer='" + getAnswer() + "'" +
            "}";
    }
}
