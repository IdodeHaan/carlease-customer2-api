package com.sogeti.carleasecustomer2api;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CarleaseCustomer2ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarleaseCustomer2ApiApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
