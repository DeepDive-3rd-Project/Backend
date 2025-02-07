package goorm.deepdive.team1.domain.addresshistory.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@RedisHash(value = "addressHistories", timeToLive = 60 * 60 * 24)
@Builder
@AllArgsConstructor
public class AddressHistoryCache {
	@Id
	private Long id;

	@Indexed
	private Long userId;

	private String regionAddress;
	private String roadAddress;

	private LocalDateTime createdAt;

	public static AddressHistoryCache create(Long id, Long userId, String regionAddress, String roadAddress, LocalDateTime createdAt) {
		return AddressHistoryCache.builder()
			.id(id)
			.userId(userId)
			.regionAddress(regionAddress)
			.roadAddress(roadAddress)
			.createdAt(createdAt)
			.build();
	}
}
