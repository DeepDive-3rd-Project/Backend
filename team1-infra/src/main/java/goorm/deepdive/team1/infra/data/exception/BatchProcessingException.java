package goorm.deepdive.team1.infra.data.exception;

import static goorm.deepdive.team1.infra.data.exception.BatchExceptionCode.BATCH_PROCESSING_FAILED;

import goorm.deepdive.team1.common.exception.CustomException;

public class BatchProcessingException extends CustomException {
	public BatchProcessingException() {
		super(BATCH_PROCESSING_FAILED);
	}
}
