package goorm.deepdive.team1.domain.addresshistory.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.addresshistory.exception.AddressHistoryNotFoundException;
import goorm.deepdive.team1.domain.addresshistory.infrastructure.AddressHistoryRepository;
import goorm.deepdive.team1.domain.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressHistoryCommandService {
	private final AddressHistoryRepository addressHistoryRepository;

	public AddressHistory create(User user, Address address) {
		AddressHistory addressHistory = AddressHistory.create(user, address);
		return addressHistoryRepository.save(addressHistory);
	}

	public void delete(Long id) {
		AddressHistory addressHistory = getById(id);
		addressHistoryRepository.delete(addressHistory);
	}

	private AddressHistory getById(Long id) {
		return addressHistoryRepository.findByIdAndDeletedAtIsNull(id)
			.orElseThrow(AddressHistoryNotFoundException::new);
	}
}
