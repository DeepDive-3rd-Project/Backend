package goorm.deepdive.team1.infra.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;

public interface JpaAddressHistoryRepository extends JpaRepository<AddressHistory, Long> {
	Optional<AddressHistory> findByIdAndDeletedAtIsNull(Long id);

	List<AddressHistory> findByUserIdAndDeletedAtIsNull(Long userId);
}
