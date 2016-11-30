package br.com.app.spring.soap;

import br.com.app.spring.soap.model.GetProductRequest;
import br.com.app.spring.soap.model.GetProductResponse;
import br.com.app.spring.soap.model.Product;
import br.com.app.spring.soap.model.SOAPConstants;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class ProductWS {

	@PayloadRoot(namespace = SOAPConstants.DEFAULT_NAMESPACE, localPart = "getProductRequest")
	@ResponsePayload
	public GetProductResponse getProduct(@RequestPayload final GetProductRequest request) {
		return new GetProductResponse(new Product(1, "Product 1"));
	}

}
