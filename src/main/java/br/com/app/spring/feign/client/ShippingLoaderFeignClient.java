package br.com.app.spring.feign.client;

import br.com.app.spring.feign.model.LoadStatus;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created on 13/01/2016.
 */
@FeignClient(name = "ShippingLoaderFeignClient", url = "localhost:8081")
public interface ShippingLoaderFeignClient {

	/**
	 * Status load status.
	 *
	 * @return the load status
	 */
	@RequestMapping(value = "/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	LoadStatus status();

	/**
	 * Status load status.
	 *
	 * @param id the id
	 * @return the load status
	 */
	@RequestMapping(value = "/status/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	LoadStatus status(@PathVariable("id") final String id);

}
