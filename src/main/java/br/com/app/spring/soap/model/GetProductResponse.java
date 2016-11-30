package br.com.app.spring.soap.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(namespace = SOAPConstants.DEFAULT_NAMESPACE)
public class GetProductResponse {

	private Product client;

}
