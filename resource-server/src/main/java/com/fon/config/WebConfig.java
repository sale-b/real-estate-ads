package com.fon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableWebSecurity
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")// Specify the API endpoint(s) for CORS configuration
                .allowedOrigins("http://client-server:8081") // Allow requests from this origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
                .allowCredentials(true)
                .allowedHeaders("*");

        registry.addMapping("/ws/**")
                .allowedOrigins("http://localhost:8080") // Add your frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);// Allow all headers
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///C:/images/");
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().mvcMatcher("/**")
                .authorizeRequests()
                .mvcMatchers("/api/v1/user/**", "/api/v1/notification/**")
                .access("hasAuthority('SCOPE_realestates.user')")
                .mvcMatchers(HttpMethod.DELETE, "/api/v1/real-estate/**")
                .access("hasAuthority('SCOPE_realestates.user')")
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }

}