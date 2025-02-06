package goorm.deepdive.team1.infra.config.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;

public interface JpaAddressHistoryRepository extends JpaRepository<AddressHistory, Long> {
	List<AddressHistory> findAllByDeletedAtIsNull();

	Optional<AddressHistory> findByIdAndDeletedAtIsNull(Long id);
}
