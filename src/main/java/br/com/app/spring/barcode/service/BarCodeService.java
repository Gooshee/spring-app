package br.com.app.spring.barcode.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

@Service
public class BarCodeService {

	private static final String TEMPLATE_LOCATION = "templates/custom.html";
	private final TemplateEngine templateEngine;

	public BarCodeService(final SpringTemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public String generateHTML(final String identificationCode, final Integer orderNumber, final LocalDate expirationDate, final BigDecimal totalAmount) {
		final String barCode = this.generateBarCode(identificationCode, orderNumber, expirationDate, totalAmount);
		final Context context = this.generateTemplateContext(barCode);
		final String htmlContent = this.templateEngine.process(TEMPLATE_LOCATION, context);
		return htmlContent.replaceAll("\n|\t", EMPTY);
	}

	private String generateBarCode(final String identificationCode, final Integer orderNumber, final LocalDate expirationDate, final BigDecimal totalAmount) {
		final BigDecimal amountInCents = totalAmount.multiply(new BigDecimal("100")).setScale(INTEGER_ZERO, ROUND_HALF_UP);
		final StringBuilder barCode = new StringBuilder(identificationCode);
		barCode.append(StringUtils.leftPad(orderNumber.toString(), 10, INTEGER_ZERO.toString()));
		barCode.append(expirationDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
		barCode.append(StringUtils.leftPad(amountInCents.toString(), 7, INTEGER_ZERO.toString()));
		barCode.append(this.generateCheckDigit(barCode.toString()));
		return barCode.toString();
	}

	private int generateCheckDigit(final String barCode) {
		int sum = 0;
		int mult = 7;
		int digit;
		for (int i = 0; i < barCode.length(); i++) {
			digit = Integer.parseInt(Character.toString(barCode.charAt(i)));
			if (mult == 1) {
				mult = 3;
			} else if (mult == 3) {
				mult = 7;
			} else {
				mult = 1;
			}
			sum += (digit * mult);
		}
		return (sum % 9) + 1;
	}

	private Context generateTemplateContext(final String barCode) {
		final Context context = new Context();
		context.setVariable("barCode", barCode);
		return context;
	}

}
