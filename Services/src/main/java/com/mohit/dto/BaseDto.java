package com.mohit.dto;

import java.math.BigDecimal;

import com.mohit.exceptions.InvalidInputFault;
import com.mohit.util.EnOperation;
import com.mohit.util.ServicesUtil;

public abstract class BaseDto {
	protected boolean isNullable = true;

	public abstract Boolean getValidForUsage();
	
	public abstract void validate(EnOperation enOperation)
			throws InvalidInputFault;

	protected void enforceMandatory(String field, Object value)
			throws InvalidInputFault {
		if (ServicesUtil.isEmpty(value)) {
			String message = "Field=" + field + " can't be empty";
			throw new InvalidInputFault(message, null);
		}
	}

	protected String checkStringSize(String field, String value, int allowedSize)
			throws InvalidInputFault {
		if (!ServicesUtil.isEmpty(value)) {// check size
			value = value.trim();
			int sizeOfInput = value.length();
			if (sizeOfInput > allowedSize) {
				String message = "Exceeding size for[" + field
						+ "] allowed size is[" + allowedSize
						+ "], input value[" + value + "] is of size[ "
						+ sizeOfInput + "]";
				throw new InvalidInputFault(message, null);
			}
			return value;
		}
		return null;
	}

	protected BigDecimal checkBigDecimalSize(String field, BigDecimal value,
			int allowedPrecision, int allowedScale) throws InvalidInputFault {
		if (value != null) {
			StringBuffer sb = new StringBuffer("1");
			while (allowedPrecision-- > 0) {
				sb.append("0");
			}
			if (value.compareTo(new BigDecimal(sb.toString())) > -1) {
				String message = "Exceeding size for field[" + field
						+ "] of allowed size[" + allowedPrecision
						+ "] and allowed decimal points[" + allowedScale
						+ "], input value[" + value + "]";
				throw new InvalidInputFault(message, null);
			}
		}
		return value;
	}

}
