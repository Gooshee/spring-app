package br.com.app.spring.barcode.controller;

import br.com.app.spring.barcode.service.BarCodeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "create", method = GET, produces = APPLICATION_JSON_VALUE)
public class BarCodeController {

	private final BarCodeService service;

	public BarCodeController(final BarCodeService service) {
		this.service = service;
	}

	@RequestMapping
	public ResponseEntity<Map<String, Object>> create(@Value("${barCode.identificationCode:80}") final String identificationCode,
	                                                  @RequestParam final Integer orderNumber,
	                                                  @RequestParam @DateTimeFormat(iso = DATE) final LocalDate expirationDate,
	                                                  @RequestParam final BigDecimal totalAmount) {
		final String generatedHTML = this.service.generateHTML(identificationCode, orderNumber, expirationDate, totalAmount);
		final Map<String, Object> response = new HashMap<>();
		response.put("htmlContent", generatedHTML);
		return ResponseEntity.ok(response);
	}

}
