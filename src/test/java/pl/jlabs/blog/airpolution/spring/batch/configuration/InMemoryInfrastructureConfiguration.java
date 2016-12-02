package pl.jlabs.blog.airpolution.spring.batch.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@EnableBatchProcessing
public class InMemoryInfrastructureConfiguration implements InfrastructureConfiguration {

    @Bean
    @Override
    public DataSource dataSource() {
	EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
	return embeddedDatabaseBuilder.addScript("classpath:sql/schema-drop-hsqldb.sql")
		.addScript("classpath:org/springframework/batch/core/schema-drop-hsqldb.sql")
	        .addScript("classpath:org/springframework/batch/core/schema-hsqldb.sql")
	        .addScript("classpath:sql/schema-hsqldb.sql")
	        .setType(EmbeddedDatabaseType.HSQL)
	        .build();
    }
}