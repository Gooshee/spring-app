package br.com.app.spring.applicationcontext.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static br.com.app.spring.applicationcontext.domain.Gateway.GatewayNames.VETEX;

@Slf4j
@Service(VETEX)
public class VetexGatewayService implements GatewayService {

	@PostConstruct
	void init() {
		log.info("Initializing {}...", this.getClass().getName());
	}

	@Override
	public String execute() {
		return this.getClass().getName();
	}

}
