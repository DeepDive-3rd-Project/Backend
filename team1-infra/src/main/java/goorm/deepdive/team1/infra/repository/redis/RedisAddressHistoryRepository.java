package goorm.deepdive.team1.infra.repository.redis;

import org.springframework.data.repository.CrudRepository;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;

public interface RedisAddressHistoryRepository extends CrudRepository<AddressHistory, Long> {
}
