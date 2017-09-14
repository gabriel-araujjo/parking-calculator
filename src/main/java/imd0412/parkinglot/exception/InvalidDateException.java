package imd0412.parkinglot.exception;

public class InvalidDateException extends Exception {
	private static final long serialVersionUID = 1L;

	private final InvalidDateType reason;

	public InvalidDateException(String msg, InvalidDateType reason) {
		super(msg);
		this.reason = reason;
	}

	public InvalidDateType getReason() {
		return reason;
	}
}
