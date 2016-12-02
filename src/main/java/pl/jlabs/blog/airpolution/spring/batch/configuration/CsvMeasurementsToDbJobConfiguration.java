package pl.jlabs.blog.airpolution.spring.batch.configuration;

import java.beans.PropertyEditor;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import pl.jlabs.blog.airpolution.domain.Measurement;

@Configuration
public class CsvMeasurementsToDbJobConfiguration {
    
    @Value("1")
    private int linesToSkip;
    
    @Value("classpath:data/*.csv")
    private Resource[] resources;
    
    @Value("#{{'time','location','pm10Concentration','pm2_5Concentration','gasConcentration'}}")
    private String[] fieldNames;
    
    @Value("insert into measurement (time, location, pm10, pm2_5, gas) values (:time, :location, :pm10Concentration, :pm2_5Concentration, :gasConcentration)")
    private String insertMeasurementSql;
    
    @Value("yyyy-MM-dd hh:mm:ss")
    private String dateFormat;

    @Autowired
    private JobBuilderFactory jobBuilders;

    @Autowired
    private StepBuilderFactory stepBuilders;
    
    @Autowired
    private InfrastructureConfiguration infrastructureConfiguration;
    
    @Bean
    public Job saveMeasurementsJob() {
	return jobBuilders.get("saveMeasurementsJob")
		.start(step())
		.build();
    }
    
    @Bean
    public Step step() {
	return stepBuilders.get("step")
	        .<Measurement, Measurement>chunk(500)
		.reader(multiResourceReader())
	        .writer(jdbcBatchItemWriter())
	        .build();
    }

    @Bean
    public ItemReader<Measurement> multiResourceReader() {
	MultiResourceItemReader<Measurement> itemReader = new MultiResourceItemReader<>();
	itemReader.setResources(resources);
	itemReader.setDelegate(cvsFileItemReader());
	return itemReader;
    }
    
    @Bean
    public FlatFileItemReader<Measurement> cvsFileItemReader() {
	FlatFileItemReader<Measurement> itemReader = new FlatFileItemReader<>();
	itemReader.setLinesToSkip(linesToSkip);
	itemReader.setLineMapper(lineMapper());
	return itemReader;
    }

    @Bean
    public LineMapper<Measurement> lineMapper() {
	DefaultLineMapper<Measurement> lineMapper = new DefaultLineMapper<>();
	lineMapper.setLineTokenizer(delimitedLineTokenizer());
	lineMapper.setFieldSetMapper(beanWrapperFieldSetMapper());
	return lineMapper;
    }


    @Bean
    public LineTokenizer delimitedLineTokenizer() {
	DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
	lineTokenizer.setNames(fieldNames);
	return lineTokenizer;
    }
    
    @Bean
    public FieldSetMapper<Measurement> beanWrapperFieldSetMapper() {
	BeanWrapperFieldSetMapper<Measurement> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
	fieldSetMapper.setTargetType(Measurement.class);
	fieldSetMapper.setCustomEditors(Collections.singletonMap(Date.class, customDateEditor()));
	return fieldSetMapper;
    }
    
    @Bean
    public PropertyEditor customDateEditor() {
	return new CustomDateEditor(new SimpleDateFormat(dateFormat), false);
    }
    
    @Bean
    public ItemWriter<Measurement> jdbcBatchItemWriter() {
	JdbcBatchItemWriter<Measurement> itemWritter = new JdbcBatchItemWriter<>();
	itemWritter.setDataSource(infrastructureConfiguration.dataSource());
	itemWritter.setSql(insertMeasurementSql);
	itemWritter.setItemSqlParameterSourceProvider(beanPropertyItemSqlParameterSourceProvider());
	return itemWritter;
    }
    
    @Bean
    public ItemSqlParameterSourceProvider<Measurement> beanPropertyItemSqlParameterSourceProvider() {
	return new BeanPropertyItemSqlParameterSourceProvider<Measurement>();
    }
}
