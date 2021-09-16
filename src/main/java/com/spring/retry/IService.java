package com.spring.retry;


interface IService {
	
	void retryService(String param) throws Exception;
	
}
