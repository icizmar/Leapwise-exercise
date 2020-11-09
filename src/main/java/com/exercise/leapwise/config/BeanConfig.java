package com.exercise.leapwise.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class BeanConfig {

    @Autowired
    JdbcTemplate jdbcTemplate;

}
