package br.com.app.spring.applicationcontext;

import br.com.app.spring.applicationcontext.domain.Gateway;
import br.com.app.spring.applicationcontext.service.GatewayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@RestController
public class GatewayController {

	private final Map<String, GatewayService> gateways;

	public GatewayController(final Map<String, GatewayService> gateways) {
		this.gateways = gateways;
	}

	@RequestMapping(value = "select-gateway", method = GET)
	public Object selectGateway(final Gateway gateway) {
		final GatewayService gatewayService = this.gateways.get(gateway.getName());
		log.info("Gateway chosen: {}", gatewayService.getClass().getName());
		return gatewayService.execute();
	}
}
