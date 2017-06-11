package lvote.mprezes.student.agh.edu.pl.repository;

import lvote.mprezes.student.agh.edu.pl.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the UserGroup entity.
 */
@SuppressWarnings("unused")
public interface UserGroupRepository extends JpaRepository<UserGroup,Long> {

    @Query("select userGroup from UserGroup userGroup where userGroup.owner.login = ?#{principal.username}")
    List<UserGroup> findByOwnerIsCurrentUser();

    @Query("select distinct userGroup from UserGroup userGroup left join fetch userGroup.members")
    List<UserGroup> findAllWithEagerRelationships();

    @Query("select userGroup from UserGroup userGroup left join fetch userGroup.members where userGroup.id =:id")
    UserGroup findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select userGroup from UserGroup userGroup join fetch userGroup.members mb where mb.login = ?#{principal.username}")
    List<UserGroup> findAllByMembersContainingCurrentUser();

}
