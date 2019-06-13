package com.mohit.exceptions;

public class NonUniqueRecordFault extends Exception {
	private static final long serialVersionUID = 7172941851377952669L;
	private MessageUIDto faultInfo;

	public NonUniqueRecordFault() {
	}

	public NonUniqueRecordFault(String faultMessage) {
		super("Failed due to corrupt data, please contact db admin ");
		faultInfo = new MessageUIDto();
		faultInfo.setMessage(faultMessage);
	}
	
	public NonUniqueRecordFault(String message, MessageUIDto faultInfo) {
		super(message);
		this.faultInfo = faultInfo;
	}

	public NonUniqueRecordFault(String message, MessageUIDto faultInfo,
			Throwable cause) {
		super(message, cause);
		this.faultInfo = faultInfo;
	}

	public MessageUIDto getFaultInfo() {
		return faultInfo;
	}
}
