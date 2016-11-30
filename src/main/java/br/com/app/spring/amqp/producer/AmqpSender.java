package br.com.app.spring.amqp.producer;

import br.com.app.spring.SpringApp;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
public class AmqpSender {

	private final RabbitTemplate rabbitTemplate;

	public AmqpSender(final RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@RequestMapping(value = "sendAmqpMessage")
	public void sendMessage(@RequestBody final String message) {
		this.rabbitTemplate.convertAndSend(SpringApp.REQUEST_QUEUE, message);
	}

}
