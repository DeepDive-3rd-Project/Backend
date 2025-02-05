package goorm.deepdive.team1.infra.config.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import goorm.deepdive.team1.domain.address.domain.Address;

public interface JpaAddressRepository extends JpaRepository<Address, Long> {
	List<Address> findAllByDeletedAtIsNull();
}
