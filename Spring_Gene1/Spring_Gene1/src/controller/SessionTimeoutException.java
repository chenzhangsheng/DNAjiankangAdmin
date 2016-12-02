package controller;

public class SessionTimeoutException extends Exception{
	private static final long serialVersionUID = 6833780018823155621L;
	
	private String message;
	
	public SessionTimeoutException(String message){
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
