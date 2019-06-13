package com.mohit.exceptions;

public class ExecutionFault extends Exception {
	private static final long serialVersionUID = -5972920879141164722L;
	private MessageUIDto faultInfo;

	public ExecutionFault() {
	}

	public ExecutionFault(String faultMessage) {
		super(faultMessage);
		faultInfo = new MessageUIDto();
		faultInfo.setMessage(faultMessage);
	}

	public ExecutionFault(String message, MessageUIDto faultInfo) {
		super(message);
		this.faultInfo = faultInfo;
	}

	public ExecutionFault(String message, MessageUIDto faultInfo,
			Throwable cause) {
		super(message, cause);
		this.faultInfo = faultInfo;
	}

	public MessageUIDto getFaultInfo() {
		return faultInfo;
	}
}
