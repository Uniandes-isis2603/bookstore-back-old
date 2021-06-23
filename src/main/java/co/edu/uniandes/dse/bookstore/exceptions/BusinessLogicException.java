package co.edu.uniandes.dse.bookstore.exceptions;

public class BusinessLogicException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public BusinessLogicException() {	
	}
	
	public BusinessLogicException(String message) {
		super(message);
	}
	
	public BusinessLogicException(Throwable cause) {
        super(cause);
    }
	
	public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }
}
