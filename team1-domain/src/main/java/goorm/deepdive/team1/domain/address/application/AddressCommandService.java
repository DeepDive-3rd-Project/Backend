package goorm.deepdive.team1.domain.address.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.address.exception.AddressNotFoundException;
import goorm.deepdive.team1.domain.address.infrastructure.AddressRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressCommandService {
	private final AddressRepository addressRepository;

	public Address create(double x, double y, String regionAddress, String roadAddress) {
		Address address = Address.create(x, y, regionAddress, roadAddress);
		return addressRepository.save(address);
	}

	public void update(Long id, double x, double y, String regionAddress, String roadAddress) {
		Address address = getAddress(id);

		address.updateX(x);
		address.updateY(y);
		address.updateRegionAddress(regionAddress);
		address.updateRoadAddress(roadAddress);
	}

	public void delete(Long id) {
		Address address = getAddress(id);
		address.markAsDeleted();
	}

	private Address getAddress(Long id) {
		return addressRepository.findByIdAndDeletedAtIsNull(id)
			.orElseThrow(AddressNotFoundException::new);
	}
}
