package goorm.deepdive.team1.domain.addresshistory.application;

import java.util.List;

import org.springframework.stereotype.Service;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.addresshistory.exception.AddressHistoryNotFoundException;
import goorm.deepdive.team1.domain.addresshistory.infrastructure.AddressHistoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressHistoryQueryService {
	private final AddressHistoryRepository addressHistoryRepository;

	public AddressHistory getById(Long id) {
		return addressHistoryRepository.findByIdAndDeletedAtIsNull(id)
			.orElseThrow(AddressHistoryNotFoundException::new);
	}

	public List<AddressHistory> getAllByDeletedAtIsNull() {
		return addressHistoryRepository.findAllByDeletedAtIsNull();
	}
}
