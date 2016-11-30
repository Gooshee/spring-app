package br.com.app.spring.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "br.com.logging")
public class SpringAppConfiguration {

	private String simpleProperty;

	private List<Integer> codigos;

}
