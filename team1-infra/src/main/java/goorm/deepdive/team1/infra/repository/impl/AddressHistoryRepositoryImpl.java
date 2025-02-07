package goorm.deepdive.team1.infra.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.addresshistory.infrastructure.AddressHistoryRepository;
import goorm.deepdive.team1.infra.repository.jpa.JpaAddressHistoryRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AddressHistoryRepositoryImpl implements AddressHistoryRepository {
	private final JpaAddressHistoryRepository jpaAddressHistoryRepository;

	@Override
	public AddressHistory save(AddressHistory addressHistory) {
		return jpaAddressHistoryRepository.save(addressHistory);
	}

	@Override
	public Optional<AddressHistory> findByIdAndDeletedAtIsNull(Long id) {
		return jpaAddressHistoryRepository.findByIdAndDeletedAtIsNull(id);
	}

	@Override
	public List<AddressHistory> findAllByDeletedAtIsNull() {
		return jpaAddressHistoryRepository.findAllByDeletedAtIsNull();
	}

	@Override
	public void delete(AddressHistory addressHistory) {
		jpaAddressHistoryRepository.delete(addressHistory);
	}
}
