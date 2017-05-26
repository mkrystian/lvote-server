package lvote.mprezes.student.agh.edu.pl.repository;

import lvote.mprezes.student.agh.edu.pl.domain.EncryptionData;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EncryptionData entity.
 */
@SuppressWarnings("unused")
public interface EncryptionDataRepository extends JpaRepository<EncryptionData,Long> {

}
