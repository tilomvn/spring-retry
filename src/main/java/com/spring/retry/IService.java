package com.spring.retry;


interface IService {
	
	void retryService(String param) throws Exception;

	String retryTemplateService(String param) throws Exception;
	
}
