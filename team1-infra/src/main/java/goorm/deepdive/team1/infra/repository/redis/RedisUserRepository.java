package goorm.deepdive.team1.infra.repository.redis;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import goorm.deepdive.team1.domain.user.domain.UserCache;

public interface RedisUserRepository extends CrudRepository<UserCache, Long> {
	Page<UserCache> findAll(Pageable pageable);
}
