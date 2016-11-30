package br.com.app.spring.jms.sender;

import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
public class JmsSender {

	private final JmsTemplate jmsTemplate;

	public JmsSender(final JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	@RequestMapping("jmsSend")
	public void send(@RequestBody final String message) {
		this.jmsTemplate.send("jms/simpleQueue", session -> session.createTextMessage(message));
	}

}
