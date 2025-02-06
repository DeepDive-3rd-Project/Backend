package goorm.deepdive.team1.domain.addresshistory.infrastructure;

import java.util.List;
import java.util.Optional;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;

public interface AddressHistoryRepository {
	AddressHistory save(AddressHistory addressHistory);

	Optional<AddressHistory> findByIdAndDeletedAtIsNull(Long id);

	List<AddressHistory> findAllByDeletedAtIsNull();

	void delete(AddressHistory addressHistory);
}
