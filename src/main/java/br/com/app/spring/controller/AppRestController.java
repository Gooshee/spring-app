package br.com.app.spring.controller;

import br.com.app.spring.configuration.SpringAppConfiguration;
import br.com.app.spring.exception.SpringAppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class AppRestController {

	@Autowired
	@Qualifier("greetings")
	private String greetings;

	private final SpringAppConfiguration configuration;

	public AppRestController(final SpringAppConfiguration configuration) {
		this.configuration = configuration;
	}

	@Cacheable("spring-app")
	@RequestMapping(value = "greetings", method = RequestMethod.GET)
	public String greetings() {
		CompletableFuture.runAsync(new Worker());
		log.info("Greeting a new user...");
		return this.greetings;
	}

	@RequestMapping(value = "simpleProperty", method = RequestMethod.GET)
	public String simpleProperty() {
		return this.configuration.getSimpleProperty();
	}

	@Cacheable("spring-app")
	@RequestMapping(value = "mirror", method = RequestMethod.GET)
	public String mirror(@RequestParam("msg") final String msg) {
		log.info("Replying message...");
		return msg;
	}

	@RequestMapping(value = "testException", method = RequestMethod.GET)
	public String testException(@RequestParam("param") final String param) {
		if ("ex".equals(param)) {
			throw new SpringAppException();
		}
		return param;
	}

	@RequestMapping(value = "testException2", method = RequestMethod.GET)
	public String testException2(@RequestParam("param") final String param) {
		if ("ex".equals(param)) {
			throw new IllegalStateException();
		}
		return param;
	}

	@RequestMapping(value = "testException3", method = RequestMethod.GET)
	public String testException3(@RequestParam("param") final String param) {
		if ("ex".equals(param)) {
			throw new IllegalArgumentException();
		}
		return param;
	}

	@RequestMapping(value = "testException4", method = RequestMethod.GET)
	public String testException4(@RequestParam("param") final String param) {
		if ("ex".equals(param)) {
			throw new RuntimeException();
		}
		return param;
	}

	@ExceptionHandler(IllegalStateException.class)
	public String handleIllegalStateException(final HttpServletRequest req, final HttpServletResponse resp, final IllegalStateException ex) {
		return "Ocorreu um erro durante o processamento do parâmetro.";
	}

}

@Slf4j
class Worker implements Runnable {

	@Override
	public void run() {
		log.info(RequestContextHolder.currentRequestAttributes().toString());
	}
}
