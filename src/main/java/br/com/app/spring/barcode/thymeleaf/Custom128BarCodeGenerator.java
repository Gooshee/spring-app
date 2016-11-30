package br.com.app.spring.barcode.thymeleaf;

import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.element.AbstractMarkupSubstitutionElementProcessor;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class Custom128BarCodeGenerator extends AbstractMarkupSubstitutionElementProcessor {

	private static final String BARCODE_ELEMENT_NAME = "barcode";
	private static final String CODE_ATTRIBUTE_NAME = "code";
	private static final int DEFAULT_PRECEDENCE = 1000;
	private static final int BARCODE_128_MOD_DIVISOR = 103;
	private static final int BARCODE_128_CHAR_CODE = 67;
	private static final int BARCODE_128_START_BASE = 38;
	private static final int BARCODE_128_STOP = 106;
	private static final int[] BARS = {212222, 222122, 222221, 121223, 121322, 131222, 122213, 122312, 132212, 221213, 221312, 231212, 112232, 122132, 122231, 113222, 123122,
			123221, 223211, 221132, 221231, 213212, 223112, 312131, 311222, 321122, 321221, 312212, 322112, 322211, 212123, 212321, 232121, 111323, 131123, 131321, 112313,
			132113, 132311, 211313, 231113, 231311, 112133, 112331, 132131, 113123, 113321, 133121, 313121, 211331, 231131, 213113, 213311, 213131, 311123, 311321, 331121,
			312113, 312311, 332111, 314111, 221411, 431111, 111224, 111422, 121124, 121421, 141122, 141221, 112214, 112412, 122114, 122411, 142112, 142211, 241211, 221114,
			413111, 241112, 134111, 111242, 121142, 121241, 114212, 124112, 124211, 411212, 421112, 421211, 212141, 214121, 412121, 111143, 111341, 131141, 114113, 114311,
			411113, 411311, 113141, 114131, 311141, 411131, 211412, 211214, 211232, 23311120};
	private int check;

	public Custom128BarCodeGenerator() {
		super(BARCODE_ELEMENT_NAME);
	}

	@Override
	protected List<Node> getMarkupSubstitutes(final Arguments arguments, final Element element) {
		final Configuration configuration = arguments.getConfiguration();
		final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
		final IStandardExpression expression = parser.parseExpression(configuration, arguments, element.getAttributeValue(CODE_ATTRIBUTE_NAME));
		final String code = (String) expression.execute(configuration, arguments);
		return this.generate128BarCodeNodes(code);
	}

	@Override
	public int getPrecedence() {
		return DEFAULT_PRECEDENCE;
	}

	private List<Node> generate128BarCodeNodes(String code) {
		final List<Node> barCodeNodes = new ArrayList<>();
		if (code.length() % 2 == 1) {
			code = "0" + code;
		}
		final String finalCode = this.parseBarCode(code);
		for (int pos = 0; pos < finalCode.length(); pos += 2) {
			final StringBuilder styleClass = new StringBuilder("div-code bar").append(finalCode.charAt(pos)).append(" space").append(finalCode.charAt(pos + 1));
			final Element barCode = new Element("div");
			barCode.setAttribute("class", styleClass.toString());
			barCodeNodes.add(barCode);
		}
		return barCodeNodes;
	}

	private String parseBarCode(final String barCode) {
		final List<Integer> bars = new ArrayList<>();
		this.addBar(bars, BARCODE_128_START_BASE + BARCODE_128_CHAR_CODE);
		for (int i = 0; i < barCode.length(); i++) {
			final int index = i++;
			final String code = barCode.substring(index, index + 2);
			this.addBar(bars, Integer.parseInt(code));
		}
		bars.add(BARS[this.check % BARCODE_128_MOD_DIVISOR]);
		bars.add(BARS[BARCODE_128_STOP]);
		return StringUtils.join(bars, EMPTY);
	}

	private void addBar(final List<Integer> bars, final int index) {
		final int nrCode = BARS[index];
		this.check = bars.size() == 0 ? index : this.check + index * bars.size();
		bars.add(nrCode);
	}
}