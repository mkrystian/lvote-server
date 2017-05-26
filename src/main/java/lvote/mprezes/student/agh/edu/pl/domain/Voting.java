package lvote.mprezes.student.agh.edu.pl.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Voting.
 */
@Entity
@Table(name = "voting")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Voting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "start_date_time", nullable = false)
    private LocalDate startDateTime;

    @NotNull
    @Column(name = "end_date_time", nullable = false)
    private LocalDate endDateTime;

    @OneToOne
    @JoinColumn(unique = true)
    private EncryptionData encryption;

    @OneToMany(mappedBy = "voting")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Vote> votes = new HashSet<>();

    @ManyToOne
    private User owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Voting name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDateTime() {
        return startDateTime;
    }

    public Voting startDateTime(LocalDate startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public void setStartDateTime(LocalDate startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDate getEndDateTime() {
        return endDateTime;
    }

    public Voting endDateTime(LocalDate endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }

    public void setEndDateTime(LocalDate endDateTime) {
        this.endDateTime = endDateTime;
    }

    public EncryptionData getEncryption() {
        return encryption;
    }

    public Voting encryption(EncryptionData encryptionData) {
        this.encryption = encryptionData;
        return this;
    }

    public void setEncryption(EncryptionData encryptionData) {
        this.encryption = encryptionData;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public Voting votes(Set<Vote> votes) {
        this.votes = votes;
        return this;
    }

    public Voting addVotes(Vote vote) {
        this.votes.add(vote);
        vote.setVoting(this);
        return this;
    }

    public Voting removeVotes(Vote vote) {
        this.votes.remove(vote);
        vote.setVoting(null);
        return this;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    public User getOwner() {
        return owner;
    }

    public Voting owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Voting voting = (Voting) o;
        if (voting.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), voting.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Voting{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startDateTime='" + getStartDateTime() + "'" +
            ", endDateTime='" + getEndDateTime() + "'" +
            "}";
    }
}
