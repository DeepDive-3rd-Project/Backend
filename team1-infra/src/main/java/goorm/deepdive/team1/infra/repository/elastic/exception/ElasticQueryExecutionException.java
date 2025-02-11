package goorm.deepdive.team1.infra.repository.elastic.exception;

import static goorm.deepdive.team1.infra.data.exception.BatchExceptionCode.BATCH_PROCESSING_FAILED;

import goorm.deepdive.team1.common.exception.CustomException;

public class ElasticQueryExecutionException extends CustomException {
	public ElasticQueryExecutionException() {
		super(BATCH_PROCESSING_FAILED);
	}
}
