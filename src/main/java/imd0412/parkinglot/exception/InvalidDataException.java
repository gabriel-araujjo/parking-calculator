package imd0412.parkinglot.exception;

public class InvalidDataException extends Exception {
	private static final long serialVersionUID = 1L;

	private final InvalidDataType reason;
	
	public InvalidDataException(InvalidDataType reason) {
		super();
		this.reason = reason;
	}

	public InvalidDataException(String msg, InvalidDataType reason) {
		super(msg);
		this.reason = reason;
	}

	public InvalidDataType getReason() {
		return reason;
	}
}
