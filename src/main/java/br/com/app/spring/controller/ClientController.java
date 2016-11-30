package br.com.app.spring.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path = "/client", method = GET)
public class ClientController {

	@Cacheable(cacheNames = "client-cache", key = "#clientName", unless = "#result == null", condition = "#springAppConfiguration == null")
	@RequestMapping(path = "/name")
	public String name(@RequestParam(required = false) final String clientName) {
		return clientName;
	}

}
