package goorm.deepdive.team1.infra.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import goorm.deepdive.team1.domain.addresshistory.domain.AddressHistory;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddressHistoryProducer {
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final ObjectMapper objectMapper;

	private static final String CREATE_ADDRESS_HISTORY = "create-address-history";

	public void sendMessageToCreate(AddressHistory addressHistory) {
		try {
			String addressHistoryJson = objectMapper.writeValueAsString(addressHistory);  // ✅ User 객체를 JSON 문자열로 변환
			kafkaTemplate.send(CREATE_ADDRESS_HISTORY, addressHistoryJson);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to serialize Address History object", e);
		}	}
}
