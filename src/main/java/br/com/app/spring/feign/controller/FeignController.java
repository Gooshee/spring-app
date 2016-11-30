package br.com.app.spring.feign.controller;

import br.com.app.spring.feign.client.ShippingLoaderFeignClient;
import br.com.app.spring.feign.model.LoadStatus;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Created on 13/01/2016.
 */
@Slf4j
@RestController
public class FeignController {

	/**
	 * The Client.
	 */
	private final ShippingLoaderFeignClient client;

	/**
	 * Instantiates a new Feign controller.
	 *
	 * @param client the client
	 */
	public FeignController(final ShippingLoaderFeignClient client) {
		this.client = client;
	}

	/**
	 * Status load status.
	 *
	 * @param id the id
	 * @return the load status
	 */
	@HystrixCommand(fallbackMethod = "fallback")
	@RequestMapping(value = {"/feignStatus", "/feignStatus/{id}"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public LoadStatus status(@PathVariable("id") final Optional<String> id) {
		if (id.isPresent()) {
			return this.client.status(id.get());
		}
		return this.client.status();
	}

	/**
	 * Fallback load status.
	 *
	 * @param id the id
	 * @return the load status
	 */
	private LoadStatus fallback(final Optional<String> id) {
		log.info("Fallback called!");
		final LoadStatus status = new LoadStatus();
		status.setConclusionMessage("Request timed out. Error 404.");
		return status;
	}

}
