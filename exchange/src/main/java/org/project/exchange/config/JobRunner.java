//package org.project.exchange.config;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.stereotype.Component;
//
//@Component
//public class JobRunner {
//
//    private final JobLauncher jobLauncher;
//    private final Job testJob;
//
//    public JobRunner(JobLauncher jobLauncher, Job testJob) {
//        this.jobLauncher = jobLauncher;
//        this.testJob = testJob;
//    }
//
//    @PostConstruct
//    public void runJob() {
//        try {
//            JobParameters jobParameters = new JobParametersBuilder()
//                    .addLong("time", System.currentTimeMillis()) // 매 실행마다 새로운 Job 파라미터 추가
//                    .toJobParameters();
//
//            JobExecution jobExecution = jobLauncher.run(testJob, jobParameters);
//            System.out.println("Job 실행 상태: " + jobExecution.getStatus());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
