package br.com.app.spring.redis.embedded;

//@Configuration
public class RedisEmbeddedConfig {

	/*private RedisServer redisServer;

	@Bean
	public JedisConnectionFactory connectionFactory() throws IOException {
		this.redisServer = new RedisServer(Protocol.DEFAULT_PORT);
		if (this.available()) {
			this.redisServer.start();
		}
		return new JedisConnectionFactory();
	}

	private boolean available() {
		try (final Socket socket = new Socket(Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT)) {
			return false;
		} catch (final IOException e) {
			return true;
		}
	}

	@PreDestroy
	void destroy() {
		this.redisServer.stop();
	}*/

}
