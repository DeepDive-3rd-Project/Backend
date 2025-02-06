package goorm.deepdive.team1.infra.config.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import goorm.deepdive.team1.domain.user.domain.User;

public interface JpaUserRepository extends JpaRepository<User, Long> {
	List<User> findAllByDeletedAtIsNull();

	Optional<User> findByIdAndDeletedAtIsNull(Long id);

	@Query("""
        SELECT u
        FROM User u
        WHERE u.deletedAt IS NULL
         	AND u.id IN (
            	SELECT ah.user.id
            	FROM AddressHistory ah
            	WHERE ah.id IN (
                	SELECT MAX(subAh.id)
                	FROM AddressHistory subAh
                	GROUP BY subAh.user.id
            	)
            	AND (
                	LOWER(ah.address.regionAddress) LIKE LOWER(CONCAT('%', :keyword, '%')) 
                	OR LOWER(ah.address.roadAddress) LIKE LOWER(CONCAT('%', :keyword, '%'))
            	)
        	)
    """)
	List<User> findUsersByAddressKeyword(@Param("keyword") String keyword);
}
