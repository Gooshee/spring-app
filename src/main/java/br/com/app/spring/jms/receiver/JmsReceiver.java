package br.com.app.spring.jms.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JmsReceiver {

	@JmsListener(destination = "jms/simpleQueue")
	public void receive(final String message) {
		log.info(message);
	}

}
