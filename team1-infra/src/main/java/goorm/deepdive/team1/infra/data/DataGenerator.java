package goorm.deepdive.team1.infra.data;

import java.util.Locale;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import net.datafaker.Faker;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import goorm.deepdive.team1.domain.user.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataGenerator {
	@PersistenceContext
	private final EntityManager entityManager;

	private static final Faker faker = new Faker(Locale.KOREA);
	private static final int BATCH_SIZE = 10000;

	@Transactional
	public Long generateData(int totalRecords) {
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < totalRecords; i++) {
			String email = faker.bothify("??????####@example.com");
			String regionAddress = faker.address().state() + " " +
				faker.address().city() + " " +
				faker.address().streetName() + " " +
				faker.address().streetAddressNumber() + "-" +
				faker.address().streetAddressNumber();
			String roadAddress = faker.address().state() + " " +
				faker.address().city() + " " +
				faker.address().streetAddress() + "길" + " " +
				faker.address().streetAddressNumber();

			User user = User.builder()
				.name(faker.name().fullName())
				.email(email)
				.phoneNumber(faker.phoneNumber().cellPhone())
				.build();
			entityManager.persist(user);

			Address address = Address.builder()
				.x(faker.number().randomDouble(6, 124, 132))
				.y(faker.number().randomDouble(6, 33, 43))
				.regionAddress(regionAddress)
				.roadAddress(roadAddress)
				.build();
			entityManager.persist(address);

			AddressHistory history = AddressHistory.builder()
				.user(user)
				.oldAddress(address)
				.newAddress(address)
				.build();
			entityManager.persist(history);

			if (i != 0 && i % BATCH_SIZE == 0) {
				entityManager.flush();
				entityManager.clear();
			}
		}

		entityManager.flush();
		entityManager.clear();

		long endTime = System.currentTimeMillis();
		return endTime - startTime;
	}
}
