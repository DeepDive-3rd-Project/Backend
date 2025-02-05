package goorm.deepdive.team1.api.address.presentation;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import goorm.deepdive.team1.api.address.application.AddressFacade;
import goorm.deepdive.team1.api.address.presentation.request.AddressCreateRequest;
import goorm.deepdive.team1.api.address.presentation.request.AddressUpdateRequest;
import goorm.deepdive.team1.api.address.presentation.resonse.AddressListResponse;
import goorm.deepdive.team1.api.address.presentation.resonse.AddressPersistResponse;
import goorm.deepdive.team1.api.address.presentation.resonse.AddressResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressControllerImpl implements AddressController {
	private final AddressFacade addressFacade;

	@Override
	@PostMapping
	public ResponseEntity<AddressPersistResponse> create(AddressCreateRequest request) {
		AddressPersistResponse response = addressFacade.create(request);
		return ResponseEntity.status(CREATED).body(response);
	}

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<AddressResponse> getById(Long id) {
		AddressResponse response = addressFacade.getById(id);
		return ResponseEntity.ok(response);
	}

	@Override
	@GetMapping
	public ResponseEntity<AddressListResponse> getAllByDeletedAtIsNull() {
		AddressListResponse response = addressFacade.getAllByDeletedAtIsNull();
		return ResponseEntity.ok(response);
	}

	@Override
	@PatchMapping("/{id}")
	public ResponseEntity<Void> update(Long id, AddressUpdateRequest request) {
		addressFacade.update(id, request);
		return ResponseEntity.noContent().build();
	}

	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(Long id) {
		addressFacade.delete(id);
		return ResponseEntity.noContent().build();
	}
}
