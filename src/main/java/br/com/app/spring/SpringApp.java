package br.com.app.spring;

import br.com.app.spring.barcode.thymeleaf.CustomDialect;
import br.com.app.spring.configuration.SpringAppConfiguration;
import br.com.app.spring.retry.problematiccall.RemoteCallService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.DefaultParameterNameProvider;
import org.mockito.Mockito;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME;
import static org.thymeleaf.templatemode.StandardTemplateModeHandlers.LEGACYHTML5;

/**
 * https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html
 */
@Slf4j
@SpringBootApplication
@EnableWs
@EnableJms
@EnableCaching
@EnableCircuitBreaker
@EnableFeignClients
@EnableJpaRepositories(basePackages = "br.com.app.spring.jpa.msaccess.model.repository")
@EnableConfigurationProperties(SpringAppConfiguration.class)
@EnableRabbit
@EnableRetry
@EnableSwagger2
@EnableScheduling
//@EnableResourceServer
public class SpringApp {

	public static final String REQUEST_QUEUE = "request-queue";
	public static final String SUCCESS_RESPONSE_QUEUE = "success-response-queue";
	public static final String ERROR_RESPONSE_QUEUE = "error-response-queue";

	protected static final String REQUEST_EXCHANGE = "request-exchange";
	protected static final String SUCCESS_RESPONSE_EXCHANGE = "success-response-exchange";
	protected static final String ERROR_RESPONSE_EXCHANGE = "error-response-exchange";

	@Value("${br.com.logging.dir}")
	private File logDir;

	public static void main(final String[] args) {
		SpringApplication.run(SpringApp.class, args);
	}

	/**
	 * ===================================================================================
	 * Test Qualifier
	 * ===================================================================================
	 */
	@Bean
	@Qualifier("greetings")
	public String createGreetings() {
		return "Seja Bem Vindo!";
	}

	/**
	 * ===================================================================================
	 * Optional and List Converter
	 * http://blog.codeleak.pl/2015/09/placeholders-support-in-value.html
	 * ===================================================================================
	 */
	@Bean
	public static ConversionService conversionService() {
		return new DefaultConversionService();
	}

	/**
	 * ===================================================================================
	 * Session Locale Resolver
	 * ===================================================================================
	 */
	@Bean
	public static LocaleResolver localeResolver() {
		final SessionLocaleResolver resolver = new SessionLocaleResolver();
		resolver.setDefaultLocale(Locale.US);
		return resolver;
	}

	/**
	 * ===================================================================================
	 * Web Configurer Adapter
	 * ===================================================================================
	 */
	@Bean
	public static WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addInterceptors(final InterceptorRegistry registry) {
				final LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
				interceptor.setParamName("lang");
				registry.addInterceptor(interceptor);
			}
		};
	}

	/**
	 * ===================================================================================
	 * Redis
	 * ===================================================================================
	 */
	@Bean
	public static CacheManager cacheManager(final RedisTemplate<?, ?> redisTemplate) {
		final RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setCachePrefix(new DefaultRedisCachePrefix("-keys-"));
		cacheManager.setUsePrefix(true);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		//		cacheManager.setDefaultExpiration(10);
		return cacheManager;
	}

	/**
	 * ===================================================================================
	 * Publishing SOAP Service with Spring WS
	 * ===================================================================================
	 */
	@Bean
	public static ServletRegistrationBean messageDispatcherServlet(final ApplicationContext applicationContext) {
		final MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/ws/*");
	}

	@Bean(name = "ProductWS")
	public static DefaultWsdl11Definition defaultWsdl11Definition(final XsdSchema productWSSchema) {
		final DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("ProductWSPort");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setTargetNamespace("http://spring-app/soap");
		wsdl11Definition.setSchema(productWSSchema);
		return wsdl11Definition;
	}

	@Bean
	public static XsdSchema productWSSchema() {
		return new SimpleXsdSchema(new ClassPathResource("ProductWS.xsd"));
	}

	/**
	 * ===================================================================================
	 * AMQP with RabbitMQ
	 * ===================================================================================
	 */
	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(final ConnectionFactory connectionFactory) {
		final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMaxConcurrentConsumers(10);
		return factory;
	}

	@Bean
	public static Map<String, Object> amqpQueueProperties() {
		final Map<String, Object> properties = new HashMap<>();
		properties.put("x-message-ttl", TimeUnit.DAYS.toMillis(7L));
		return properties;
	}

	/**
	 * ***************************** QUEUES *****************************
	 */
	@Bean
	public static Queue requestQueue(@Qualifier("amqpQueueProperties") final Map<String, Object> amqpQueueProperties) {
		return new Queue(REQUEST_QUEUE, true, false, false, amqpQueueProperties);
	}

	@Bean
	public static Queue successResponseQueue(@Qualifier("amqpQueueProperties") final Map<String, Object> amqpQueueProperties) {
		return new Queue(SUCCESS_RESPONSE_QUEUE, true, false, false, amqpQueueProperties);
	}

	@Bean
	public static Queue errorResponseQueue(@Qualifier("amqpQueueProperties") final Map<String, Object> amqpQueueProperties) {
		return new Queue(ERROR_RESPONSE_QUEUE, true, false, false, amqpQueueProperties);
	}

	/**
	 * ***************************** EXCHANGES *****************************
	 */
	@Bean
	public static DirectExchange requestExchange() {
		return new DirectExchange(REQUEST_EXCHANGE);
	}

	@Bean
	public static DirectExchange successResponseExchange() {
		return new DirectExchange(SUCCESS_RESPONSE_EXCHANGE);
	}

	@Bean
	public static DirectExchange errorResponseExchange() {
		return new DirectExchange(ERROR_RESPONSE_EXCHANGE);
	}

	/**
	 * ***************************** BINDINGS *****************************
	 */
	@Bean
	public static Binding requestBinding(final Queue requestQueue, final DirectExchange requestExchange) {
		return BindingBuilder.bind(requestQueue).to(requestExchange).with(REQUEST_QUEUE);
	}

	@Bean
	public static Binding successResponseBinding(final Queue successResponseQueue, final DirectExchange successResponseExchange) {
		return BindingBuilder.bind(successResponseQueue).to(successResponseExchange).with(SUCCESS_RESPONSE_QUEUE);
	}

	@Bean
	public static Binding errorResponseBinding(final Queue errorResponseQueue, final DirectExchange errorResponseExchange) {
		return BindingBuilder.bind(errorResponseQueue).to(errorResponseExchange).with(ERROR_RESPONSE_QUEUE);
	}

	/**
	 * ===================================================================================
	 * Retry
	 * ===================================================================================
	 */
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public static RemoteCallService remoteCallService() {
		final RemoteCallService mockService = Mockito.mock(RemoteCallService.class);
		Mockito.when(mockService.call(Mockito.anyString())).then(invocation -> {
			log.info(LocalTime.now().toString());
			final String message = (String) invocation.getArguments()[0];
			if (message.contains("ERROR")) {
				throw new RuntimeException("Test Exception!");
			}
			return "Your message: " + message + "!";
		});
		return mockService;
	}

	/**
	 * ===================================================================================
	 * Customizing Validation ParameterNameProvider
	 * ===================================================================================
	 */
	@Bean
	public static MethodValidationPostProcessor methodValidationPostProcessor() {
		final DefaultParameterNameProvider paramNameProvider = new DefaultParameterNameProvider() {
			@Override
			public List<String> getParameterNames(final Method method) {
				return Arrays.stream(method.getParameters()).map(Parameter::getName).collect(Collectors.toList());
			}
		};
		final ValidatorFactory factory = Validation.byDefaultProvider().configure().parameterNameProvider(paramNameProvider).buildValidatorFactory();
		final MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
		processor.setValidatorFactory(factory);
		return processor;
	}

	/**
	 * ===================================================================================
	 * Swagger 2
	 * http://heidloff.net/article/usage-of-swagger-2-0-in-spring-boot-applications-to-document-apis/
	 * <p>
	 * Swagger UI
	 * http://localhost:8081/swaggerui/index.html#
	 * http://localhost:8081/v2/api-docs?group={groupName}
	 * ===================================================================================
	 */
	@Bean
	public static Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("messages")
				.apiInfo(apiInfo())
				.select()
				.paths(PathSelectors.regex("/messages.*"))
				.build();
	}

	private static ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Spring App with Swagger")
				.description("Documenting with Swagger")
				.version("1.0.0")
				.build();
	}

	/**
	 * ===================================================================================
	 * BarCode
	 * Thymeleaf Custom Dialect and Markup Substitution Element Processor
	 * ===================================================================================
	 */
	@Bean
	public static SpringTemplateEngine templateEngine() {
		final ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setTemplateMode(LEGACYHTML5.getTemplateModeName());
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(resolver);
		templateEngine.addDialect(new CustomDialect());
		return templateEngine;
	}

	/**
	 * ===================================================================================
	 * Get HttpServletRequest from others Threads
	 * ===================================================================================
	 */
	@Bean
	public ServletRegistrationBean dispatcherServletRegistration() {
		final ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet());
		registration.setLoadOnStartup(0);
		registration.setName(DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);
		registration.addInitParameter("threadContextInheritable", "true");
		return registration;
	}

	@Bean
	public DispatcherServlet dispatcherServlet() {
		return new DispatcherServlet();
	}
}
