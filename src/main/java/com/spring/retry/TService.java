package com.spring.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;


@Service
class TService implements IService {

	Logger log = LoggerFactory.getLogger(getClass());
	

	/**
	 * Retry if Exception occurred, retry 2 times and delay 500ms for each retry time.
	 * Use listeners instead of value to use RetryContext.
	 */
	//@Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(500), recover = "recoverRetryService")
	@Retryable(listeners = "retryListener", maxAttempts = 3, backoff = @Backoff(500), recover = "recoverRetryService1")
	@Override
	public void retryService(String param) throws Exception {
		if ("num".equals(param)) {
			throw new Exception("Ex");
		}
		log.info(param);
	}
	
	@Recover
	void recoverRetryService(Exception ex, String param) {
		log.info(ex.getMessage());
		log.info("Recover: " + param);
	}
	
	@Recover
	void recoverRetryService1(Exception ex, String param) {
		log.info(ex.getMessage());
		log.info("Recover1: " + param);
	}

}
