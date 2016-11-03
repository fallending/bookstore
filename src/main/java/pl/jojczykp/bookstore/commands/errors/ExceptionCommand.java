package pl.jojczykp.bookstore.commands.errors;

public class ExceptionCommand {

	private String message;
	private String stackTraceAsString;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStackTraceAsString() {
		return stackTraceAsString;
	}

	public void setStackTraceAsString(String stackTraceAsString) {
		this.stackTraceAsString = stackTraceAsString;
	}

	public String toString() {
		return "message: " + message + "\nstackTrace: " + stackTraceAsString;
	}
}
