package com.deliverhealth.codingchallenge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Value("${token}")
    private String token;
    @Bean
    public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
        return builder.additionalInterceptors((ClientHttpRequestInterceptor) (request, body, execution) -> {
                    request.getHeaders().setBearerAuth(token);
                    return execution.execute(request, body);
                }).build();
    }

    @Bean
    public ObjectMapper getMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        return mapper;
    }
}
