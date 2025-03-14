//package org.project.exchange.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@Slf4j
//@Configuration
//public class TestStepConfiguration {
//    @Bean
//    public Step testStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
//        return new StepBuilder("testStep", jobRepository)
//                .tasklet(
//                        (contribution, chunkContext) -> {
//                            log.info("testStep is running");
//                            return RepeatStatus.FINISHED;
//                        },
//                        platformTransactionManager
//                )
//                .build();
//    }
//}
