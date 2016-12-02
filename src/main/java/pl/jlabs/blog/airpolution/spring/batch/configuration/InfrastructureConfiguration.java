package pl.jlabs.blog.airpolution.spring.batch.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;

public interface InfrastructureConfiguration {
 
    @Bean
    public DataSource dataSource();
 
}