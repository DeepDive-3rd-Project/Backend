package goorm.deepdive.team1.infra.kafka.exception;

import static goorm.deepdive.team1.infra.kafka.exception.KafkaExceptionCode.KAFKA_PROCESSING_FAILED;

import goorm.deepdive.team1.common.exception.CustomException;

public class KafkaProcessingException extends CustomException {
	public KafkaProcessingException() {
		super(KAFKA_PROCESSING_FAILED);
	}
}
