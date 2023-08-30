package com.fon.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "destination")
@Data
@Configuration
public class DestinationProperties {

	private String events;
	private String removals;

}
