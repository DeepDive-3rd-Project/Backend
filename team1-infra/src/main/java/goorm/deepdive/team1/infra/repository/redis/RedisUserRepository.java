package goorm.deepdive.team1.infra.repository.redis;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import goorm.deepdive.team1.domain.user.domain.UserCache;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisUserRepository {
	private final RedisTemplate<String, Object> redisTemplate;

	public Page<UserCache> findAllSorted(Pageable pageable) {
		long start = pageable.getOffset();
		long end = start + pageable.getPageSize() - 1;

		Set<Object> userIds = redisTemplate.opsForZSet().range("user:zset", start, end);
		if (userIds == null || userIds.isEmpty()) {
			return Page.empty();
		}

		List<Object> userList = redisTemplate.opsForValue().multiGet(
			userIds.stream().map(id -> "users:" + id).collect(Collectors.toList())
		);

		List<UserCache> users = userList.stream()
			.filter(Objects::nonNull)
			.map(obj -> (UserCache) obj)
			.collect(Collectors.toList());

		Long totalElements = redisTemplate.opsForZSet().size("user:zset");

		return new PageImpl<>(users, pageable, totalElements != null ? totalElements : 0);
	}

	public UserCache findById(Long id) {
		return (UserCache)redisTemplate.opsForValue().get("users:" + id);
	}

	public void save(UserCache userCache) {
		redisTemplate.opsForValue().set("users:" + userCache.getId(), userCache);
		redisTemplate.opsForZSet().add("user:zset", userCache.getId(), userCache.getId());
	}
}
