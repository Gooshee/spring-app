package br.com.app.spring.retry.service;

import br.com.app.spring.retry.problematiccall.RemoteCallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Slf4j
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CallService {

	private final RemoteCallService remoteCallService;

	public CallService(final RemoteCallService remoteCallService) {
		this.remoteCallService = remoteCallService;
	}

	@Retryable(backoff = @Backoff(delay = 1000L, multiplier = 2))
	public String call(final String message) {
		log.info(LocalTime.now().toString());
		return this.remoteCallService.call(message);
	}

}
