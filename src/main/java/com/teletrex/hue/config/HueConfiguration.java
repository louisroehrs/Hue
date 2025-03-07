package com.teletrex.hue.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
    @PropertySource(value = "file:${user.home}/.hue/hueconfig.properties", ignoreResourceNotFound = true),
    @PropertySource("classpath:application.properties")
})
public class HueConfiguration {
    // This class enables loading of both the stored configuration and application.properties
} 