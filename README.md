# Spring Retry
* Normally using @Retrable and @Recovery in service context is best choice
* If need a retry context which use shared object between retries, use RetryListener.