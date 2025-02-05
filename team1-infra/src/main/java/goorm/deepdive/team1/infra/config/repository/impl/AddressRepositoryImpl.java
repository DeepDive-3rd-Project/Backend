package goorm.deepdive.team1.infra.config.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import goorm.deepdive.team1.domain.address.domain.Address;
import goorm.deepdive.team1.domain.address.infrastructure.AddressRepository;
import goorm.deepdive.team1.infra.config.repository.jpa.JpaAddressRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AddressRepositoryImpl implements AddressRepository {
	private final JpaAddressRepository jpaAddressRepository;

	@Override
	public Address save(Address address) {
		return jpaAddressRepository.save(address);
	}

	@Override
	public Optional<Address> findById(Long id) {
		return jpaAddressRepository.findById(id);
	}

	@Override
	public List<Address> findAllByDeletedAtIsNull() {
		return jpaAddressRepository.findAllByDeletedAtIsNull();
	}

	@Override
	public void delete(Address address) {
		jpaAddressRepository.delete(address);
	}

}
