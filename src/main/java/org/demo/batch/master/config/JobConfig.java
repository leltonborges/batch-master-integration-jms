package org.demo.batch.master.config;

import org.demo.batch.master.common.Constants.Master;
import org.demo.batch.master.common.Constants.Worker;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.partition.RemotePartitioningManagerStepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessagingTemplate;

@Configuration
public class JobConfig {
    private final RemotePartitioningManagerStepBuilderFactory remoteStepBuilder;

    @Autowired
    public JobConfig(RemotePartitioningManagerStepBuilderFactory remoteStepBuilder) {
        this.remoteStepBuilder = remoteStepBuilder;
    }

    @JobScope
    @Bean("stepMaster")
    public Step step(@Qualifier("partitionerConfig") Partitioner partitioner,
//                     @Qualifier("directChannel") DirectChannel channel,
                     MessagingTemplate messagingTemplate) {
        return remoteStepBuilder.get(Master.STEP_NAME)
                                .partitioner(Worker.STEP_REMOTE_PARTITION_NAME, partitioner)
//                                .partitionHandler(handler)
                                .messagingTemplate(messagingTemplate)
//                                .outputChannel(channel)
                                .build();
    }

    @Bean
    public Job remotePartitioningOneWayJob(JobRepository jobRepository,
                                           @Qualifier("stepMaster") Step step) {
        return new JobBuilder(Master.JOB_NAME)
                .repository(jobRepository)
                .start(step)
                .build();
    }
}
