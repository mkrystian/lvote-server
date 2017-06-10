package lvote.mprezes.student.agh.edu.pl.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A VotingContent.
 */
@Entity
@Table(name = "voting_content")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VotingContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "question", nullable = false)
    private String question;

    @OneToOne(mappedBy = "content")
    @JsonIgnore
    private Voting voting;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public VotingContent question(String question) {
        this.question = question;
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Voting getVoting() {
        return voting;
    }

    public VotingContent voting(Voting voting) {
        this.voting = voting;
        return this;
    }

    public void setVoting(Voting voting) {
        this.voting = voting;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VotingContent votingContent = (VotingContent) o;
        if (votingContent.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), votingContent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VotingContent{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            "}";
    }
}
