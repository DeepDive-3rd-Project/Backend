package goorm.deepdive.team1.domain.address.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.address.exception.AddressNotFoundException;
import goorm.deepdive.team1.domain.address.infrastructure.AddressRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressQueryService {
	private final AddressRepository addressRepository;

	public Address getById(Long id) {
		return addressRepository.findByIdAndDeletedAtIsNull(id)
			.orElseThrow(AddressNotFoundException::new);
	}

	public List<Address> getAllByDeletedAtIsNull() {
		return addressRepository.findAllByDeletedAtIsNull();
	}

	public Address getByAddress(String address) {
		return addressRepository.findByRegionAddressOrRoadAddressAndDeletedAtIsNull(address)
			.orElseThrow(AddressNotFoundException::new);
	}

}
