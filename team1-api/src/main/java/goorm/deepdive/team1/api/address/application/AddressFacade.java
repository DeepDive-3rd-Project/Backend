package goorm.deepdive.team1.api.address.application;

import java.util.List;

import org.springframework.stereotype.Component;

import goorm.deepdive.team1.api.address.presentation.request.AddressCreateRequest;
import goorm.deepdive.team1.api.address.presentation.request.AddressUpdateRequest;
import goorm.deepdive.team1.api.address.presentation.resonse.AddressListResponse;
import goorm.deepdive.team1.api.address.presentation.resonse.AddressPersistResponse;
import goorm.deepdive.team1.api.address.presentation.resonse.AddressResponse;
import goorm.deepdive.team1.domain.address.application.AddressCommandService;
import goorm.deepdive.team1.domain.address.application.AddressQueryService;
import goorm.deepdive.team1.domain.address.domain.Address;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddressFacade {
	private final AddressQueryService addressQueryService;
	private final AddressCommandService addressCommandService;

	public AddressPersistResponse create(AddressCreateRequest request) {
		Address address = addressCommandService.create(request.x(), request.y(), request.regionAddress(), request.roadAddress(), request.region());
		return AddressPersistResponse.from(address);
	}

	public AddressResponse getById(Long id) {
		Address address = addressQueryService.getById(id);
		return AddressResponse.from(address);
	}

	public AddressListResponse getAllByDeletedAtIsNull() {
		List<Address> addressList = addressQueryService.getAllByDeletedAtIsNull();
		return AddressListResponse.from(addressList);
	}

	public void update(Long id, AddressUpdateRequest request) {
		addressCommandService.update(id, request.x(), request.y(), request.regionAddress(), request.roadAddress(), request.region());
	}

	public void delete(Long id) {
		addressCommandService.delete(id);
	}
}
