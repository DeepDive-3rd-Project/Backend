package goorm.deepdive.team1.domain.user.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@RedisHash(value = "users", timeToLive = 60 * 60 * 24)
@Builder
@AllArgsConstructor
public class UserCache {
	@Id
	private Long id;

	@Indexed
	private String name;

	private String email;

	private String phoneNumber;

	private String latestRegionAddress;
	private String latestRoadAddress;


	public static UserCache create(Long id, String name, String email, String phoneNumber, String latestRegionAddress, String latestRoadAddress) {
		return UserCache.builder()
			.id(id)
			.name(name)
			.email(email)
			.phoneNumber(phoneNumber)
			.latestRegionAddress(latestRegionAddress)
			.latestRoadAddress(latestRoadAddress)
			.build();
	}
}
