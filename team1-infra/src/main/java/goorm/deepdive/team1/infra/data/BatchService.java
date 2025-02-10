package goorm.deepdive.team1.infra.data;

import static java.util.Locale.KOREA;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import net.datafaker.Faker;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistoryCache;
import goorm.deepdive.team1.domain.user.domain.UserCache;
import goorm.deepdive.team1.infra.repository.redis.RedisAddressHistoryRepository;
import goorm.deepdive.team1.infra.repository.redis.RedisUserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BatchService {
	private final JdbcTemplate jdbcTemplate;
	private final RedisUserRepository redisUserRepository;
	private final RedisAddressHistoryRepository redisAddressHistoryRepository;
	private static final Faker faker = new Faker(KOREA);

	@Async("taskExecutor")
	public CompletableFuture<Void> insertBatchAsync(int batchSize) {
		insertBatch(batchSize);
		return CompletableFuture.completedFuture(null);
	}

	public void insertBatch(int total) {
		String insertUserSql = "INSERT INTO \"user\" (name, email, phone_number, created_at, updated_at) VALUES (?, ?, ?, ?, ?) RETURNING id";
		String insertAddressSql = "INSERT INTO address (x, y, region_address, road_address, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
		String insertHistorySql = "INSERT INTO address_history (user_id, address_id, created_at, updated_at) VALUES (?, ?, ?, ?) RETURNING id";

		for (int i = 0; i < total; i++) {
			String name = faker.name().fullName();
			String email = faker.bothify("??????####@example.com");
			String phoneNumber = faker.phoneNumber().cellPhone();

			Long userId = jdbcTemplate.queryForObject(insertUserSql, new Object[]{
				name,
				email,
				phoneNumber,
				Timestamp.valueOf(LocalDateTime.now()),
				Timestamp.valueOf(LocalDateTime.now())
			}, Long.class);

			String regionAddress = faker.address().state() + " " +
				faker.address().city() + " " +
				faker.address().streetName() + " " +
				faker.address().streetAddressNumber() + "-" +
				faker.address().streetAddressNumber();

			String roadAddress = faker.address().state() + " " +
				faker.address().city() + " " +
				faker.address().streetAddress() + "길 " +
				faker.address().streetAddressNumber();

			Long addressId = jdbcTemplate.queryForObject(insertAddressSql, new Object[]{
				faker.number().randomDouble(6, 124, 132),
				faker.number().randomDouble(6, 33, 43),
				regionAddress,
				roadAddress,
				Timestamp.valueOf(LocalDateTime.now()),
				Timestamp.valueOf(LocalDateTime.now())
			}, Long.class);

			Long addressHistoryId = jdbcTemplate.queryForObject(insertHistorySql, new Object[]{
				userId,
				addressId,
				Timestamp.valueOf(LocalDateTime.now()),
				Timestamp.valueOf(LocalDateTime.now())
			}, Long.class);;

			UserCache userCache = UserCache
				.create(userId, name, email, phoneNumber, regionAddress, roadAddress);
			redisUserRepository.save(userCache);

			AddressHistoryCache addressHistoryCache = AddressHistoryCache
				.create(addressHistoryId, userId, regionAddress, roadAddress, LocalDateTime.now());
			redisAddressHistoryRepository.save(addressHistoryCache);
		}
		System.out.println(total + " 개 성공!");
	}
}
