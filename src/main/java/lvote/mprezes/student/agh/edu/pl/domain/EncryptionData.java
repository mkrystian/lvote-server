package lvote.mprezes.student.agh.edu.pl.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EncryptionData.
 */
@Entity
@Table(name = "encryption_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EncryptionData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "private_key")
    private String privateKey;

    @Column(name = "public_key")
    private String publicKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public EncryptionData privateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public EncryptionData publicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EncryptionData encryptionData = (EncryptionData) o;
        if (encryptionData.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), encryptionData.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EncryptionData{" +
            "id=" + getId() +
            ", privateKey='" + getPrivateKey() + "'" +
            ", publicKey='" + getPublicKey() + "'" +
            "}";
    }
}
