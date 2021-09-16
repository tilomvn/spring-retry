package com.spring.retry;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableRetry
public class RetryConfig {
	
//	@Bean
//	RetryTemplate retryTemplate(RetryListener retryListener) {
//		RetryTemplate retryTemplate = new RetryTemplate();
//		
//		FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
//        fixedBackOffPolicy.setBackOffPeriod(500l);
//        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
//        
//        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
//        retryPolicy.setMaxAttempts(3);
//        retryTemplate.setRetryPolicy(retryPolicy);
//        
//        retryTemplate.registerListener(retryListener);
//        
//		return retryTemplate;
//	}

	
	@Bean
	RetryListener retryListener() {
		RetryListener retryListener = new RetryListener() {
			
			Logger log = LoggerFactory.getLogger(getClass());
			
			int retryCount = 0;
			
			@Override
			public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
				log.info("open");
				callback = new RetryCallback<T, E>() {
					@Override
					public T doWithRetry(RetryContext context) throws E {
						log.info("doWithRetry");
						return null;
					}
				};
				return true;
			}
			
			@Override
			public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
				context.setAttribute("retryCount", retryCount++);
				log.info("retry: " + retryCount);
				try {
					callback.doWithRetry(context);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
				log.info("close");
				log.info("retryCount: " + retryCount);
			}
		};
		
		return retryListener;
	}
}
