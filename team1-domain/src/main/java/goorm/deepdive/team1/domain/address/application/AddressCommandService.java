package goorm.deepdive.team1.domain.address.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.address.infrastructure.AddressRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressCommandService {
	private final AddressRepository addressRepository;
	private final KakaoApiAddressService kakaoApiAddressService;

	public Address create(double x, double y, String regionAddress, String roadAddress, String region) {
		Address address = Address.create(x, y, regionAddress, roadAddress, region);
		return addressRepository.save(address);
	}

	public Address save(Address address) {
		return addressRepository.save(address);
	}

	public void update(Address address, double x, double y, String regionAddress, String roadAddress, String region) {
		address.updateX(x);
		address.updateY(y);
		address.updateRegionAddress(regionAddress);
		address.updateRoadAddress(roadAddress);
		address.updateRegion(region);
	}

	public void delete(Address address) {
		address.markAsDeleted();
	}

	@Transactional
	public Address findOrCreateAddress(String regionAddress, String roadAddress) {
		return addressRepository.findByRegionAddressOrRoadAddressAndDeletedAtIsNull(regionAddress, roadAddress)
			.orElseGet(() -> createAddressFromKakaoApi(roadAddress));
	}

	private Address createAddressFromKakaoApi(String roadAddress) {
		Address newAddress = kakaoApiAddressService.getGeoDataFromAddress(roadAddress);
		return addressRepository.save(newAddress);
	}
}
