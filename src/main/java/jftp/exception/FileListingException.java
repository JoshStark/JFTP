package jftp.exception;

@SuppressWarnings("serial")
public class FileListingException extends RuntimeException {

	public FileListingException(String message, Exception cause) {
		super(message, cause);
	}
}
