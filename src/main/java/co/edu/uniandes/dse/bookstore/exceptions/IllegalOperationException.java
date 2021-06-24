package co.edu.uniandes.dse.bookstore.exceptions;

public class IllegalOperationException extends Exception {

	private static final long serialVersionUID = 1L;

	public IllegalOperationException() {	
	}
	
	public IllegalOperationException(String message) {
		super(message);
	}
	
	public IllegalOperationException(Throwable cause) {
        super(cause);
    }
	
	public IllegalOperationException(String message, Throwable cause) {
        super(message, cause);
    }

}
