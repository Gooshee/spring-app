package br.com.app.spring.retry.controller;

import br.com.app.spring.retry.service.CallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CallController {

	private final CallService callService;

	public CallController(final CallService retryService) {
		this.callService = retryService;
	}

	@RequestMapping(value = "call", produces = APPLICATION_JSON_VALUE)
	public String call(@RequestParam final String message) {
		log.info(LocalTime.now().toString());
		return this.callService.call(message);
	}
}
