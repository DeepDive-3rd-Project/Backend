package goorm.deepdive.team1.api.address.presentation.resonse;

import goorm.deepdive.team1.domain.address.domain.Address;
import lombok.Builder;

@Builder
public record AddressPersistResponse(
	Long id
) {
	public static AddressPersistResponse from(Address address) {
		return AddressPersistResponse.builder()
			.id(address.getId())
			.build();
	}
}
