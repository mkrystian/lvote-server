package lvote.mprezes.student.agh.edu.pl.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @OneToOne
    @JoinColumn(unique = true)
    private VotingContent content;

    @OneToOne
    @JoinColumn(unique = true)
    private EncryptionData encryption;

    @ManyToOne
    private User owner;

    @OneToMany()
    @JoinColumn(name = "voting_id")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Vote> votes = new HashSet<>();


    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "voting_already_voted",
        joinColumns = @JoinColumn(name = "votings_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "already_voteds_id", referencedColumnName = "id"))
    private Set<User> alreadyVoteds = new HashSet<>();

    @ManyToOne
    private UserGroup userGroup;

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public Voting startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Voting endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public VotingContent getContent() {
        return content;
    }

    public Voting content(VotingContent votingContent) {
        this.content = votingContent;
        return this;
    }

    public void setContent(VotingContent votingContent) {
        this.content = votingContent;
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

    public Set<User> getAlreadyVoteds() {
        return alreadyVoteds;
    }

    public Voting alreadyVoteds(Set<User> users) {
        this.alreadyVoteds = users;
        return this;
    }

    public Voting addAlreadyVoted(User user) {
        this.alreadyVoteds.add(user);
        return this;
    }

    public Voting removeAlreadyVoted(User user) {
        this.alreadyVoteds.remove(user);
        return this;
    }

    public void setAlreadyVoteds(Set<User> users) {
        this.alreadyVoteds = users;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public Voting userGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
        return this;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
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

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
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
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
