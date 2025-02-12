package goorm.deepdive.team1.infra.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import goorm.deepdive.team1.domain.user.domain.User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserProducer {
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final ObjectMapper objectMapper;

	private static final String CREATE_USER = "create-user";

	public void sendMessageToCreate(User user) {
		try {
			String userJson = objectMapper.writeValueAsString(user);  // ✅ User 객체를 JSON 문자열로 변환
			kafkaTemplate.send(CREATE_USER, userJson);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to serialize User object", e);
		}
	}
}
