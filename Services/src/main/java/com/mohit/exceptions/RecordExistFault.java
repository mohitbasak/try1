package com.mohit.exceptions;

public class RecordExistFault extends Exception {
	private static final long serialVersionUID = 2680366978983580640L;
	private MessageUIDto faultInfo;

	public RecordExistFault() {
	}

	public RecordExistFault(String faultMessage) {
		super("Record already exist ");
		faultInfo = new MessageUIDto();
		faultInfo.setMessage(faultMessage);
	}

	public RecordExistFault(String message, MessageUIDto faultInfo) {
		super(message);
		this.faultInfo = faultInfo;
	}

	public RecordExistFault(String message, MessageUIDto faultInfo,
			Throwable cause) {
		super(message, cause);
		this.faultInfo = faultInfo;
	}

	public MessageUIDto getFaultInfo() {
		return faultInfo;
	}

}
