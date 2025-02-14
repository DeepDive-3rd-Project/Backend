package goorm.deepdive.team1.infra.repository.redis;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistoryCache;

public interface RedisAddressHistoryRepository extends CrudRepository<AddressHistoryCache, Long> {
	List<AddressHistoryCache> findByUserId(Long userId);
}
