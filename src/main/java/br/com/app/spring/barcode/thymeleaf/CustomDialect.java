package br.com.app.spring.barcode.thymeleaf;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.Collections;
import java.util.Set;

public class CustomDialect extends AbstractDialect {

	public static final String DIALECT_NAMESPACE = "http://spring-app-custom.com";
	private static final String DIALECT_PREFIX = "custom";

	@Override
	public String getPrefix() {
		return DIALECT_PREFIX;
	}

	@Override
	public Set<IProcessor> getProcessors() {
		return Collections.singleton(new Custom128BarCodeGenerator());
	}
}
