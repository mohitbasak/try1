package com.mohit.exceptions;

public class InvalidInputFault extends Exception {
	private static final long serialVersionUID = 2680366978983580640L;
	private MessageUIDto faultInfo;

	public InvalidInputFault() {
	}

	public InvalidInputFault(String faultMessage) {
		super(faultMessage);
		faultInfo = new MessageUIDto();
		faultInfo.setMessage(faultMessage);
	}
	
	public InvalidInputFault(String message, MessageUIDto faultInfo) {
		super(message);
		this.faultInfo = faultInfo;
	}

	public InvalidInputFault(String message, MessageUIDto faultInfo,
			Throwable cause) {
		super(message, cause);
		this.faultInfo = faultInfo;
	}

	public MessageUIDto getFaultInfo() {
		return faultInfo;
	}
}
