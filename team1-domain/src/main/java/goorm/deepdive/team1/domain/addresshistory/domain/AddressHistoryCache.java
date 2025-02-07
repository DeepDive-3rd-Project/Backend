package goorm.deepdive.team1.domain.addresshistory.domain;

import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@RedisHash(value = "addressHistories", timeToLive = 60 * 60 * 24)
@Builder
@AllArgsConstructor
public class AddressHistoryCache {

}
