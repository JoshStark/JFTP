package jftp.exception;


@SuppressWarnings("serial")
public class UploadFailureException extends RuntimeException {

    public UploadFailureException(String message) {
        super(message);
    }
}
