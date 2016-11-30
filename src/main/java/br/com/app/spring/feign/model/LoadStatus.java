package br.com.app.spring.feign.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * The type Load status.
 */
@Data
@NoArgsConstructor
public class LoadStatus {

	/**
	 * The Id.
	 */
	private String id;
	/**
	 * The Country.
	 */
	private String country;
	/**
	 * The Start time.
	 */
	private LocalDateTime startTime;
	/**
	 * The Finish time.
	 */
	private LocalDateTime finishTime;
	/**
	 * The Total parts.
	 */
	private Integer totalParts;
	/**
	 * The Parts complete.
	 */
	private Integer partsComplete;
	/**
	 * The Conclusion message.
	 */
	private String conclusionMessage;
	/**
	 * The Successful.
	 */
	private boolean successful;
	/**
	 * The In progress.
	 */
	private boolean inProgress;
}
