package br.com.crosscut.android.myfinances.bo;

public class MyFinanceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8667029834668691335L;
	
	public MyFinanceException() {
		super();
	} 
	
	public MyFinanceException(String message) {
		super(message);
	}
	
	public MyFinanceException(String message, Throwable cause) {
		super(message,cause);
	}

	public MyFinanceException(Throwable cause) {
		super(cause);
	}

}
