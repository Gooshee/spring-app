package br.com.app.spring.amqp.consumer;

import br.com.app.spring.SpringApp;
import br.com.app.spring.retry.service.CallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Slf4j
@Component
//@RabbitListener(bindings = @QueueBinding(value = @Queue(SpringApp.SIMPLE_QUEUE), exchange = @Exchange(value = SpringApp.SIMPLE_EXCHANGE, type = ExchangeTypes.TOPIC, durable =
// "true")))
@RabbitListener(queues = SpringApp.REQUEST_QUEUE)
public class RequestMessageConsumer {

	private final RabbitTemplate rabbitTemplate;
	private final CallService callService;
	private String id;

	public RequestMessageConsumer(final RabbitTemplate rabbitTemplate, final CallService callService) {
		this.rabbitTemplate = rabbitTemplate;
		this.callService = callService;
	}

	@PostConstruct
	void init() {
		this.id = UUID.randomUUID().toString();
	}

	@RabbitHandler
	public void receiveRequestMessage(final String message) {
		log.info(this.id);
		try {
			final String response = this.callService.call(message);
			this.rabbitTemplate.convertAndSend(SpringApp.SUCCESS_RESPONSE_QUEUE, message);
		} catch (final Exception e) {
			this.rabbitTemplate.convertAndSend(SpringApp.ERROR_RESPONSE_QUEUE, "An error has occorred: " + e.getMessage());
		}
	}

}
