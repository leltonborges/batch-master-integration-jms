package org.demo.batch.master.resource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/demo")
public class ClientResource {

    private final JobLauncher jobLauncher;
    private final Job job;

    @Autowired
    public ClientResource(JobLauncher jobLauncher,
                          @Qualifier("remotePartitioningOneWayJob") Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @GetMapping
    public ResponseEntity<?> demo() {
        try {
            this.jobLauncher.run(job, parameters());
            return ResponseEntity.ok().build();
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }

    private JobParameters parameters() {
        return new JobParametersBuilder().addDate("date", new Date())
                                         .addLong("time", System.currentTimeMillis())
                                         .toJobParameters();
    }
}
