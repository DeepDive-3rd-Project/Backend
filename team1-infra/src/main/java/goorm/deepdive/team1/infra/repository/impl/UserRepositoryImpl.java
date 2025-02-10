package goorm.deepdive.team1.infra.repository.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.domain.UserCache;
import goorm.deepdive.team1.domain.user.domain.UserDocument;
import goorm.deepdive.team1.domain.user.infrastructure.UserRepository;
import goorm.deepdive.team1.infra.repository.elastic.ElasticUserRepository;
import goorm.deepdive.team1.infra.repository.jpa.JpaUserRepository;
import goorm.deepdive.team1.infra.repository.redis.RedisUserRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final JpaUserRepository jpaUserRepository;
	private final RedisUserRepository redisUserRepository;
	private final ElasticUserRepository elasticUserRepository;

	@Override
	public User save(User user) {
		return jpaUserRepository.save(user);
	}

	@Override
	public Optional<User> findByIdAndDeletedAtIsNull(Long id) {
		return jpaUserRepository.findByIdAndDeletedAtIsNull(id);
	}

	@Override
	public UserCache getUserCache(Long id) {
		return redisUserRepository.findById(id);
	}

	@Override
	public Page<UserCache> findAll(Pageable pageable) {
		return redisUserRepository.findAllSorted(pageable);
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
	public Page<UserDocument> searchByRoadAddress(String keyword, Pageable pageable) {
		return elasticUserRepository.searchByRoadAddress(keyword, pageable);
	}

	@Override
	public Page<UserDocument> searchByRegionAddress(String keyword, Pageable pageable) {
		return elasticUserRepository.searchByRegionAddress(keyword, pageable);
	}

	@Override
	public Page<UserDocument> searchByName(String name, Pageable pageable) {
		return elasticUserRepository.searchByName(name, pageable);
	}
}
