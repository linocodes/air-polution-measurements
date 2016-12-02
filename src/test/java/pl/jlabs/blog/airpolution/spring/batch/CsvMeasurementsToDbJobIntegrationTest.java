package pl.jlabs.blog.airpolution.spring.batch;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.jlabs.blog.airpolution.spring.batch.configuration.CsvMeasurementsToDbJobConfiguration;
import pl.jlabs.blog.airpolution.spring.batch.configuration.InMemoryInfrastructureConfiguration;

@ContextConfiguration(classes={InMemoryInfrastructureConfiguration.class, CsvMeasurementsToDbJobConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class CsvMeasurementsToDbJobIntegrationTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private JobRepository jobRepository;

    @Test
    public void testLaunchJob() throws Exception {
	JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
	jobLauncher.run(job, jobParameters);
	assertThat(jobRepository.getLastJobExecution("saveMeasurementsJob", jobParameters).getExitStatus(),
	        is(ExitStatus.COMPLETED));
    }
    
}
