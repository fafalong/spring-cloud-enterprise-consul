package com.sc.gateway.controller;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * 路由限流配置
 * 
 */
@Configuration
public class RateLimiterConfig {

	@Bean(value = "remoteAddrKeyResolver")
	public KeyResolver remoteAddrKeyResolver() {
		return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
	}

	@Bean(value = "urlPathKeyResolver")
	KeyResolver urlPathKeyResolver() {
		return exchange -> Mono.just(exchange.getRequest().getPath().value());
	}

	@Bean(value = "userKeyResolver")
	KeyResolver userKeyResolver() {
		return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("userId"));
	}

}