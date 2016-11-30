package br.com.app.spring.retry.problematiccall;

public interface RemoteCallService {

	String call(final String message) throws RuntimeException;

}
