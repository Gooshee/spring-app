package br.com.app.spring.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleTaskExecutor {

	/**
	 * At second 0
	 * At minute 0
	 * At first hour
	 * At first day of month
	 * On january
	 * On monday
	 */
	@Scheduled(cron = "0 0 1 1 1 MON")
	public void execute() {
		log.info("============================= Executed! =============================");
	}

}
