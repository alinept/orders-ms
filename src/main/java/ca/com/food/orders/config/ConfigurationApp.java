package ca.com.food.orders.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class ConfigurationApp {

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
