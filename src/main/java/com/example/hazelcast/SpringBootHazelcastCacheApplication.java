package com.example.hazelcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringBootHazelcastCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootHazelcastCacheApplication.class, args);
	}

}
