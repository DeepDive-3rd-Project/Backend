package goorm.deepdive.team1.infra.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import goorm.deepdive.team1.domain.user.domain.User;
import goorm.deepdive.team1.domain.user.infrastructure.UserRepository;
import goorm.deepdive.team1.infra.repository.jpa.JpaUserRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final JpaUserRepository jpaUserRepository;

	@Override
	public User save(User user) {
		return jpaUserRepository.save(user);
	}

	@Override
	public Optional<User> findByIdAndDeletedAtIsNull(Long id) {
		return jpaUserRepository.findByIdAndDeletedAtIsNull(id);
	}

	@Override
	public List<User> findAllByDeletedAtIsNull() {
		return jpaUserRepository.findAllByDeletedAtIsNull();
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
	public List<User> findUsersByRoadAddressKeyword(String keyword) {
		return jpaUserRepository.findUsersByRoadAddressKeyword(keyword);
	}

	@Override
	public List<User> findUsersByRegionAddressKeyword(String keyword) {
		return jpaUserRepository.findUsersByRegionAddressKeyword(keyword);
	}
}
