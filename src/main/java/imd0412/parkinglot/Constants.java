package imd0412.parkinglot;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Constants {
	public static final String VALID_DATE_FORMAT = "yyyy.MM.dd HH:mm";

	private static final String VALID_DATE_REGEX = "(\\d+)\\.(\\d+)\\.(\\d+) (\\d+):(\\d+)";

	public static final Pattern DATE_PATTERN;

	public static final SimpleDateFormat DATE_FORMATTER;

	public static Set<Integer> MONTHS_31 = new HashSet<>();

	static {
		DATE_PATTERN = Pattern.compile(VALID_DATE_REGEX);

		DATE_FORMATTER = new SimpleDateFormat(Constants.VALID_DATE_FORMAT);

		MONTHS_31.add(1);
		MONTHS_31.add(3);
		MONTHS_31.add(5);
		MONTHS_31.add(7);
		MONTHS_31.add(8);
		MONTHS_31.add(10);
		MONTHS_31.add(12);


	}
}
