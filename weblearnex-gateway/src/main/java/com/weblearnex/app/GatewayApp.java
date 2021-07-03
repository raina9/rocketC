package com.weblearnex.app;

import com.weblearnex.app.filter.ErrorFilter;
import com.weblearnex.app.filter.PostFilter;
import com.weblearnex.app.filter.PreFilter;
import com.weblearnex.app.filter.RouteFilter;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
@EnableEurekaClient
@EntityScan("com.weblearnex.app.*")
public class GatewayApp {
	public static void main(String[] args) {
		SpringApplication.run(GatewayApp.class, args);
	}


	@Bean
	public ErrorFilter errorFilter() {
		return new ErrorFilter();
	}

	@Bean
	public PostFilter postFilter() {
		return new PostFilter();
	}
	@Bean
	public PreFilter preFilter() {
		return new PreFilter();
	}

	@Bean
	public RouteFilter routeFilter() {
		return new RouteFilter();
	}

}
