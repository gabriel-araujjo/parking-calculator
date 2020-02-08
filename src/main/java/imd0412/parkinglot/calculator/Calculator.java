package imd0412.parkinglot.calculator;

import imd0412.parkinglot.Constants;
import imd0412.parkinglot.ParkingLotType;
import imd0412.parkinglot.PaymentRule;
import imd0412.parkinglot.exception.DateFormatException;
import imd0412.parkinglot.exception.InvalidDateException;
import imd0412.parkinglot.exception.InvalidDateType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Matcher;

public class Calculator {

    /**
     * External method
     * @param checkIn The check in date
     * @param checkOut The check out date
     * @param type VIP, LongTerm or ShortTerm
     * @return The final cost of the parking period
     * @throws InvalidDateException When a data is invalid
     * @throws DateFormatException When the string is mal formatted
     */
    public Float calcParkingCost (String checkIn, String checkOut, ParkingLotType type) throws InvalidDateException, DateFormatException {
        return calculateParkingCost(checkIn, checkOut, type);
    }

	/**
	 * Calculates the staying cost in the parking lot.
	 * 
	 * @param checkin String representing check-in date. String follows the format "yyyy.MM.dd HH:mm".
	 * @param checkout String representing check-out date. String follows the format "yyyy.MM.dd HH:mm".
	 * @param type VIP, LongTerm or ShortTerm
	 * @return The cost of parking in period
	 */
	Float calculateParkingCost(String checkin, String checkout, ParkingLotType type) throws DateFormatException, InvalidDateException {
		Duration dur = parseDuration(checkin, checkout);

        return PaymentRule.of(type).calcCost(dur);
	}

    /**
     * Parse the check in and check out string into a duration.
     * @param checkin The check in date
     * @param checkout The checkout date
     * @return The duration between {@code checkin} and {@code checkout}
     * @throws DateFormatException If {@code checkin} or {@code checkout} is not in right format
     * @throws InvalidDateException If {@code checkin} or {@code checkout} is not a valid date, or
     *                              if {@code checkout} is before then {@code checkin}
     */
	private Duration parseDuration(String checkin, String checkout) throws DateFormatException, InvalidDateException {
		Matcher matcherIn = Constants.DATE_PATTERN.matcher(checkin.trim());
		Matcher matcherOut = Constants.DATE_PATTERN.matcher(checkout.trim());

		validateIntervalFormat(matcherIn, matcherOut);

		LocalDateTime checkInDateTime = parseDateTime(matcherIn);
		LocalDateTime checkOutDateTime = parseDateTime(matcherOut);

		if (checkInDateTime.getYear() < 1970 || checkInDateTime.getYear() > 2017) {
			throw new InvalidDateException("Check in year (" + checkInDateTime.getYear() + ") out of valid range [1970, 2017].", InvalidDateType.InvalidYear);
		}

		if (checkOutDateTime.getYear() < 1970 || checkOutDateTime.getYear() > 2018) {
			throw new InvalidDateException("Check out year (" + checkOutDateTime.getYear() + ") out of valid range [1970, 2018].", InvalidDateType.InvalidYear);
		}

		if (checkOutDateTime.isBefore(checkInDateTime)) {
		    throw new InvalidDateException(checkout + " is before then " + checkin, InvalidDateType.InvalidDay);
        }

		return Duration.between(checkInDateTime, checkOutDateTime);
	}

    /**
     * Parse a valid date matcher into a {@link LocalDateTime}
     * @param dateTimeMatcher A valid date matcher
     * @return A localDateTime equivalent to the dataTimeMatcher
     * @throws InvalidDateException If the matcher contains an invalid date. Ex.: February 30
     */
	private LocalDateTime parseDateTime(Matcher dateTimeMatcher) throws InvalidDateException {
		int year = Integer.parseInt(dateTimeMatcher.group(1));
		int month = Integer.parseInt(dateTimeMatcher.group(2));
		int day = Integer.parseInt(dateTimeMatcher.group(3));
		int hour = Integer.parseInt(dateTimeMatcher.group(4));
		int minute = Integer.parseInt(dateTimeMatcher.group(5));

		if (month > 12 || month < 1) {
			throw new InvalidDateException("Month " + month +  " out of range", InvalidDateType.InvalidMonth);
		}

		boolean leapYear = year % 400 == 0 || year % 4 == 0 && year % 100 != 0;

		if (day < 1 || day > 31) {
			throw new InvalidDateException("Day " + day + " out of valid range", InvalidDateType.InvalidDay);
		}

		if (month == 2 && day > (leapYear ? 29 : 28)) {
			throw new InvalidDateException("" + year + "." + month + "." + day + " does not exists", InvalidDateType.NonexistentDate);
		}

		if (!Constants.MONTHS_31.contains(month) && day > 30) {
			throw new InvalidDateException("" + year + "." + month + "." + day + " does not exists", InvalidDateType.NonexistentDate);
		}

		if (hour > 23) {
		    throw new InvalidDateException("" + hour + " is not a valid hour", InvalidDateType.NonexistentDate);
        }

        if (minute > 59) {
            throw new InvalidDateException("" + minute + " is not a valid minute", InvalidDateType.NonexistentDate);
        }


		return LocalDateTime.of(year, month, day, hour, minute);
	}

    /**
     * Check whether both matcherIn and matcherOut are well formatted dates.
     * @param matcherIn checkin matcher
     * @param matcherOut checkout matcher
     * @throws DateFormatException When whether matcherIn or matcherOut is mal formatted.
     *
     * @see Constants#VALID_DATE_REGEX
     */
	private void validateIntervalFormat(Matcher matcherIn, Matcher matcherOut) throws DateFormatException {
		StringBuilder builder = null;
		if (!matcherIn.matches()) {
			builder = new StringBuilder("Check in date is not in the valid format");
		}
		if (!matcherOut.matches()) {
			if (builder == null) {
				builder = new StringBuilder();
			} else {
				builder.append("\n");
			}
			builder.append("Check out date is not in a valid format");
		}

		if (builder != null) {
			throw new DateFormatException(builder.toString());
		}
	}
}
