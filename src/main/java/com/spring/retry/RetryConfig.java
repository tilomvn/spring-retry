package com.spring.retry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;


@Configuration
@EnableRetry
public class RetryConfig {
	
	@Bean
	RetryTemplate retryTemplate(/*RetryListener retryListener*/) {
        return RetryTemplate.builder().fixedBackoff(500l).maxAttempts(3).build();
	}
	
	//@Bean
	IService service(RetryTemplate retryTemplate) {
		return new TService(retryTemplate);
	}
	
	/**
	 * Retry Listener
	 * @param service: could not inject without creating bean in this class ?
	 */
	//@Bean
	RetryListener retryListener(TService service) {
		RetryListener retryListener = new RetryListener() {
			
			/**
			 * @callback is method in Service.
			 */
			@Override
			public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
				service.log.info("open");
				return true;
			}
			
			@Override
			public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
				//context.setAttribute("retryCount", retryCount++);
				//service.log.info("retry: " + retryCount);
				service.log.info(context.toString());
			}
			
			@Override
			public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
				service.log.info("close");
				//service.log.info("retryCount: " + retryCount);
				service.log.info(context.toString());
			}
		};
		
		return retryListener;
	}
}
