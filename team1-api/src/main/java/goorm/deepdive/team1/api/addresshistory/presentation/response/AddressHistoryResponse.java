package goorm.deepdive.team1.api.addresshistory.presentation.response;

import java.time.LocalDateTime;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistoryCache;
import lombok.Builder;

@Builder
public record AddressHistoryResponse (
	String roadAddress,
	String regionAddress,
	LocalDateTime createdAt
) {
	public static AddressHistoryResponse from(AddressHistoryCache addressHistoryCache) {
		return AddressHistoryResponse.builder()
			.roadAddress(addressHistoryCache.getRoadAddress())
			.regionAddress(addressHistoryCache.getRegionAddress())
			.createdAt(addressHistoryCache.getCreatedAt())
			.build();
	}

	public static AddressHistoryResponse from(AddressHistory addressHistory) {
		Address address = addressHistory.getAddress();
		return AddressHistoryResponse.builder()
			.roadAddress(address.getRoadAddress())
			.regionAddress(address.getRegionAddress())
			.createdAt(addressHistory.getCreatedAt())
			.build();
	}
}
