package com.spring.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;


@Service
class TService implements IService {

	Logger log = LoggerFactory.getLogger(getClass());
	
	final RetryTemplate retryTemplate;
	
	TService(RetryTemplate retryTemplate) {
		this.retryTemplate = retryTemplate;
	}
	
	/**
	 * Using Annotation
	 * Retry if Exception occurred, retry 2 times and delay 500ms for each retry time.
	 * Use listeners instead of value to use RetryContext.
	 */
//	@Retryable(listeners = "retryListener", maxAttempts = 3, backoff = @Backoff(500), recover = "recoverRetryService1")
	@Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(500), recover = "recoverRetryService")
	@Override
	public void retryService(String param) throws Exception {
		if ("num".equals(param)) {
			log.info("retryAnnotationService");
			throw new Exception("Ex retryAnnotationService");
		}
		log.info(param);
	}
	@Recover
	void recoverRetryService(Throwable ex, String param) {
		log.info(ex.getMessage());
		log.info("retryAnnotationService Recover: " + param);
	}
	@Recover
	void recoverRetryService1(Throwable ex, String param) {
		log.info(ex.getMessage());
		log.info("retryAnnotationService Recover1: " + param);
	}
	
	/**
	 * Using RetryTemplate is the best choice because we can access RetryContext inside invoker.
	 */
	@Override
	public String retryTemplateService(String param) throws Exception {
		return retryTemplate.execute((retryContext) -> {
			if ("num".equals(param)) {
				log.info("retryTemplateService: " + retryContext.toString());
				throw new Exception("Ex retryTemplateService");
			}
			return param;
		}, (recoveryContext) -> { //Recovery
			log.info(recoveryContext.getLastThrowable().getMessage());
			log.info("retryTemplateService Recover: " + param);
			return null;
		});
	}
}
