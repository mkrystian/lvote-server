package lvote.mprezes.student.agh.edu.pl.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @OneToMany(mappedBy = "votingContent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<VotingAnswer> answers = new HashSet<>();

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

    public Set<VotingAnswer> getAnswers() {
        return answers;
    }

    public VotingContent answers(Set<VotingAnswer> votingAnswers) {
        this.answers = votingAnswers;
        return this;
    }

    public VotingContent addAnswers(VotingAnswer votingAnswer) {
        this.answers.add(votingAnswer);
        votingAnswer.setVotingContent(this);
        return this;
    }

    public VotingContent removeAnswers(VotingAnswer votingAnswer) {
        this.answers.remove(votingAnswer);
        votingAnswer.setVotingContent(null);
        return this;
    }

    public void setAnswers(Set<VotingAnswer> votingAnswers) {
        this.answers = votingAnswers;
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
