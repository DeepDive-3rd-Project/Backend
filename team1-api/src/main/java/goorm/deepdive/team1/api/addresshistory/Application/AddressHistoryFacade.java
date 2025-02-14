package goorm.deepdive.team1.api.addresshistory.Application;

import java.util.List;

import org.springframework.stereotype.Component;

import goorm.deepdive.team1.api.addresshistory.presentation.response.AddressHistoryListResponse;
import goorm.deepdive.team1.domain.addresshistory.application.AddressHistoryQueryService;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistoryCache;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddressHistoryFacade {
	private final AddressHistoryQueryService addressHistoryQueryService;

	public AddressHistoryListResponse getAddressHistories(Long userId) {
		List<AddressHistoryCache> cachedHistories = addressHistoryQueryService.getAddressHistoryCaches(userId);

		return !cachedHistories.isEmpty()
			? AddressHistoryListResponse.fromCaches(cachedHistories)
			: AddressHistoryListResponse.fromEntity(
			addressHistoryQueryService.getByUserIdDeletedAtIsNull(userId)
		);
	}
}
