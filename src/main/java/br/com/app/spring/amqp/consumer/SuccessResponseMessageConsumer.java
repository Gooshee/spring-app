package br.com.app.spring.amqp.consumer;

import br.com.app.spring.SpringApp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Slf4j
@Component
public class SuccessResponseMessageConsumer {

	private String id;

	@PostConstruct
	void init() {
		this.id = UUID.randomUUID().toString();
	}

	@RabbitListener(queues = SpringApp.SUCCESS_RESPONSE_QUEUE)
	public void receiveSuccessResponseMessage(final Message message) {
		log.info("{}: {}", this.id, new String(message.getBody()));
	}

}
