package org.project.exchange.batch;

import java.util.List;

import jakarta.persistence.EntityManagerFactory;

import org.project.exchange.model.currency.Currency;
import org.project.exchange.model.currency.service.CurrencyService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfiguration {

	private final CurrencyService currencyService;

	public BatchConfig(CurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	@Bean
	public Job fetchCurrencyJob(JobRepository jobRepository, Step clearCurrencyStep, Step fetchCurrencyStep) {
		return new JobBuilder("fetchCurrencyJob", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(fetchCurrencyStep)
				//.next(fetchCurrencyStep)
				.build();
	}


	@Bean
	public Step fetchCurrencyStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("fetchCurrencyStep", jobRepository)
				.tasklet(fetchCurrencyTasklet(), transactionManager)
				.build();
	}

	@Bean
	public Tasklet fetchCurrencyTasklet() {
		return (contribution, chunkContext) -> {
			try {
				List<Currency> saved = currencyService.fetchAndSaveCurrency();
				if (saved.isEmpty()) {
					throw new IllegalStateException("환율 데이터가 비어 있습니다.");
				}
				log.info("환율 데이터 수집 완료: {}건", saved.size());
			} catch (Exception e) {
				log.error("환율 데이터 수집 실패", e);
				throw e;
			}
			return RepeatStatus.FINISHED;
		};
	}
}

