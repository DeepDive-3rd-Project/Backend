package goorm.deepdive.team1.api.address.presentation.resonse;

import java.util.List;

import goorm.deepdive.team1.domain.address.domain.Address;
import lombok.Builder;

@Builder
public record AddressListResponse(
	List<AddressResponse> addresses
) {
	public static AddressListResponse from(List<Address> addressList) {
		return AddressListResponse.builder()
			.addresses(addressList.stream().map(AddressResponse::from).toList())
			.build();
	}
}
