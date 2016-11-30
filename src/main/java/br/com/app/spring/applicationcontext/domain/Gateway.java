package br.com.app.spring.applicationcontext.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gateway {

	VETEX(GatewayNames.VETEX),
	INTELLI_POST(GatewayNames.INTELLI_POST),
	NETSHOES(GatewayNames.NETSHOES);

	private String name;

	public interface GatewayNames {
		String VETEX = "VETEX";
		String INTELLI_POST = "INTELLI_POST";
		String NETSHOES = "NETSHOES";
	}

}
