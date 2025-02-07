package goorm.deepdive.team1.infra.repository.redis;

import org.springframework.data.repository.CrudRepository;

import goorm.deepdive.team1.domain.user.domain.User;

public interface RedisUserRepository extends CrudRepository<User, Long> {
}
