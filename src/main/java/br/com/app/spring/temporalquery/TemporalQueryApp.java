package br.com.app.spring.temporalquery;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQuery;
import java.util.ArrayList;
import java.util.List;

public class TemporalQueryApp {

	private static final List<LocalDate> HOLIDAYS = new ArrayList<>();
	private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("America/Sao_Paulo");

	static {
		final LocalTime defaultTime = LocalTime.of(0, 0, 0, 0);

		// Carnaval
		HOLIDAYS.add(LocalDate.of(2015, Month.FEBRUARY, 9));

		// Natal
		HOLIDAYS.add(LocalDate.of(2015, Month.JANUARY, 25));

		// Ano Novo
		HOLIDAYS.add(LocalDate.of(2015, Month.JANUARY, 1));
	}

	public static void main(final String[] args) {
		final Instant startInstant = Instant.parse("2016-01-01T02:00:00Z");
		final Instant endInstant = Instant.parse("2016-01-31T02:00:00Z");

		System.out.println(startInstant);
		System.out.println(endInstant);

		final long startMilli = startInstant.toEpochMilli();
		final long endMilli = endInstant.toEpochMilli();

		System.out.println(startMilli);
		System.out.println(endMilli);

		final TemporalQuery<Boolean> holidayQuery = temporal -> HOLIDAYS.contains(((LocalDate) temporal));
		final TemporalQuery<Boolean> weekendQuery = temporal -> {
			final DayOfWeek dayOfWeek = ((LocalDate) temporal).getDayOfWeek();
			return DayOfWeek.SATURDAY.equals(dayOfWeek) || DayOfWeek.SUNDAY.equals(dayOfWeek);
		};

		final LocalDate from = Instant.ofEpochMilli(startMilli).atZone(DEFAULT_ZONE_ID).toLocalDate();
		final LocalDate to = Instant.ofEpochMilli(endMilli).atZone(DEFAULT_ZONE_ID).toLocalDate();
		final List<LocalDate> validDates = new ArrayList<>();

		for (LocalDate currentDate = from; !currentDate.isAfter(to); currentDate = currentDate.plusDays(1)) {
			if (!currentDate.query(holidayQuery) && !currentDate.query(weekendQuery)) {
				validDates.add(currentDate);
			}
		}

		validDates.forEach(date -> System.out.println(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

		System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("' ('hh:mm'-'")));
		System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm')'")));
	}

}

