package goorm.deepdive.team1.infra.repository.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.domain.UserCache;
import goorm.deepdive.team1.domain.user.infrastructure.UserRepository;
import goorm.deepdive.team1.infra.repository.jpa.JpaUserRepository;
import goorm.deepdive.team1.infra.repository.redis.RedisUserRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final JpaUserRepository jpaUserRepository;
	private final RedisUserRepository redisUserRepository;

	@Override
	public User save(User user) {
		return jpaUserRepository.save(user);
	}

	@Override
	public Optional<User> findByIdAndDeletedAtIsNull(Long id) {
		return jpaUserRepository.findByIdAndDeletedAtIsNull(id);
	}

	@Override
	public Optional<UserCache> getUserCache(Long id) {
		return redisUserRepository.findById(id);
	}

	@Override
	public Page<UserCache> findAll(Pageable pageable) {
		return redisUserRepository.findAll(pageable);
	}

	@Override
	public void deleteById(Long id) {
		jpaUserRepository.deleteById(id);
	}

	@Override
	public boolean existsById(Long id) {
		return jpaUserRepository.existsById(id);
	}

	@Override
	public Page<User> findUsersByRoadAddressKeyword(String keyword, Pageable pageable) {
		return jpaUserRepository.findUsersByRoadAddressKeyword(keyword, pageable);
	}

	@Override
	public Page<User> findUsersByRegionAddressKeyword(String keyword, Pageable pageable) {
		return jpaUserRepository.findUsersByRegionAddressKeyword(keyword, pageable);
	}
}
