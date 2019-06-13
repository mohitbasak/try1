package com.mohit.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.mohit.exceptions.InvalidInputFault;

public class ServicesUtil {

	public static final String NOT_APPLICABLE = "N/A";
	public static final String SPECIAL_CHAR = "∅";
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	public static final int lookupStartIndex = 0;
	public static final int lookupBatchSize = 501;

	public static boolean isEmpty(Object[] objs) {
		if (objs == null || objs.length == 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(Object o) {
		if (o == null) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(Collection<?> o) {
		if (o == null || o.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.trim().isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(Integer num) {
		if ((num == null) || (num == 0)) {
			return true;
		}
		return false;
	}

	public static String getCSV(Object... objs) {
		if (!isEmpty(objs)) {
			if (objs[0] instanceof Collection<?>) {
				return getCSVArr(((Collection<?>) objs[0]).toArray());
			} else {
				return getCSVArr(objs);
			}

		} else {
			return "";
		}
	}

	public static String logInputValuesFromInputObject(Object fromClass) {
		Field[] fieldNames = fromClass.getClass().getDeclaredFields();
		if (!isEmpty(fieldNames)) {
			char newLine = '\n';
			StringBuffer sb = new StringBuffer("I/P: ");
			for (int i = 0; i < fieldNames.length; i++) {
				try {
					sb.append(fieldNames[i]).append(": ");
					sb.append(extractStr(fieldNames[i].get(fromClass))).append(newLine);
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				}
			}
			return sb.toString();
		} else {
			return "";
		}
	}

	private static String getCSVArr(Object[] objs) {
		if (!isEmpty(objs)) {
			StringBuffer sb = new StringBuffer();
			for (Object obj : objs) {
				sb.append(',');
				if (obj instanceof Field) {
					sb.append(extractFieldName((Field) obj));
				} else {
					sb.append(extractStr(obj));
				}
			}
			sb.deleteCharAt(0);
			return sb.toString();
		} else {
			return "";
		}
	}

	public static String extractStr(Object o) {
		return o == null ? "" : o.toString();
	}

	public static String extractFieldName(Field o) {
		return o == null ? "" : o.getName();
	}

	public static String buildNoRecordMessage(String queryName, Object... parameters) {
		StringBuffer sb = new StringBuffer("No Record found for query: ");
		sb.append(queryName);
		if (!isEmpty(parameters)) {
			sb.append(" for params:");
			sb.append(getCSV(parameters));
		}
		return sb.toString();
	}

	public static String appendLeadingChars(String input, char c, int finalSize) throws InvalidInputFault {
		StringBuffer strBuffer = new StringBuffer();
		if (input == null) {
			return null;
		}
		int paddingSize = finalSize - input.length();
		if (paddingSize < 0) {
			throw new InvalidInputFault(
					getCSV("Value passed is greater than count:" + input + " count is: " + finalSize));
		}
		while (paddingSize-- > 0) {
			strBuffer.append(c);
		}
		strBuffer.append(input);

		return strBuffer.toString();
	}

	public static String appendTrailingChars(String input, char c, int finalSize) throws InvalidInputFault {
		StringBuffer strBuffer = new StringBuffer();
		if (input == null) {
			input = "";
		}
		int paddingSize = finalSize - input.length();
		if (paddingSize < 0) {
			throw new InvalidInputFault(
					getCSV("Value passed is greater than count:" + input + " count is: " + finalSize));
		}
		strBuffer.append(input);
		while (paddingSize-- > 0) {
			strBuffer.append(c);
		}
		String result = strBuffer.toString();
		return result;
	}

	public static String appendTrailingChars(Object value, char c, int finalSize) throws InvalidInputFault {
		StringBuffer strBuffer = new StringBuffer();
		String input = "";
		if (!ServicesUtil.isEmpty(value)) {
			input = value.toString();
		}
		if (input == null) {
			input = "";
		}
		int paddingSize = finalSize - input.length();
		if (paddingSize < 0) {
			throw new InvalidInputFault(
					getCSV("Value passed is greater than count:" + input + " count is: " + finalSize));
		}
		strBuffer.append(input);
		while (paddingSize-- > 0) {
			strBuffer.append(c);
		}
		String result = strBuffer.toString();
		return result;
	}

	public static String appendLeadingZeroetoString(String materialNumber, int count) {
		StringBuffer materialNumberBuffer = new StringBuffer();
		if (!ServicesUtil.isEmpty(materialNumber)) {
			int paddingSize = count - materialNumber.length();
			for (int i = 0; i < paddingSize; i++) {
				materialNumberBuffer.append("0");
			}
			materialNumberBuffer.append(materialNumber);
		}
		return materialNumberBuffer.toString();
	}

	public static String getCodeForDisplayValue(String displayValue) {
		if (displayValue != null && displayValue.contains(",")) {
			return displayValue.substring(0, displayValue.indexOf(","));
		}
		return displayValue;
	}

	public static Integer convertStringToInteger(String field, String value) throws InvalidInputFault {
		boolean isEmpty = isEmpty(value);
		if (isEmpty) {
			return null;
		} else {
			value = value.trim();
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				String message = "Invalid value for field=" + field + " with value=(" + value + ")";
				throw new InvalidInputFault(message, null);
			}
		}
	}

	public static BigDecimal convertStringToBigDecimal(String field, String value) throws InvalidInputFault {
		boolean isEmpty = isEmpty(value);
		if (isEmpty) {
			return null;
		} else {
			value = value.trim();
			try {
				return new BigDecimal(value);
			} catch (NumberFormatException e) {
				String message = "Invalid value for field=" + field + " with value=(" + value + ")";
				throw new InvalidInputFault(message, null);
			}
		}
	}

	public static BigDecimal convertIntegerToBigDecimal(String fieldName, Integer value) throws InvalidInputFault {
		boolean isEmpty = isEmpty(value);
		if (isEmpty)
			return null;
		else {
			try {
				return new BigDecimal(value);
			} catch (NumberFormatException e) {
				String message = "Invalid value for field=" + fieldName + " with value=(" + value + ")";
				throw new InvalidInputFault(message, null);
			}
		}
	}

	public static boolean convertStringToBoolean(String field, String value) {
		if (value == null || value.trim().isEmpty()) {
			return false;
		}
		return true;
	}

	public static String replaceWithPercentSign(String str) {
		return (str == null || str.trim().isEmpty()) ? "%%" : str.trim().replace("*", "%");
	}

	public static boolean compare(Object a, Object b) {
		if (a == null && b == null) {
			return true;
		}

		if (a == null || b == null) {
			return false;
		}
		return a.equals(b);
	}

	public static void enforceMandatory(String field, Object value) throws InvalidInputFault {
		if (ServicesUtil.isEmpty(value)) {
			String message = "Field=" + field + " can't be empty";
			throw new InvalidInputFault(message, null);
		}
	}

	public static String getNewNodeId(String oldNodeId, Integer stepNumber) {
		if (oldNodeId == null || stepNumber == null)
			return null;
		else
			return oldNodeId + "." + stepNumber;
	}

	public static String getOldNodeId(String newNodeId) {
		if (newNodeId == null || !newNodeId.contains(".")) {
			return newNodeId;
		} else {
			int endIndex = newNodeId.lastIndexOf('.');
			return newNodeId.substring(0, endIndex);
		}
	}

	public static Integer getStepNumber(String newNodeId) {
		if (newNodeId == null || !newNodeId.contains(".")) {
			return null;
		} else {
			int beginIndex = newNodeId.lastIndexOf('.');
			String substring = newNodeId.substring(beginIndex + 1);
			try {
				return Integer.parseInt(substring);
			} catch (NumberFormatException e) {
				return null;
			}
		}
	}

	public static String getPeriodIndicatorForDisplay(String str) {
		if (isEmpty(str) || ServicesUtil.SPECIAL_CHAR.equals(str)) {
			return "D";
		} else if (str.equals("1")) {
			return "W";
		} else if (str.equals("2")) {
			return "M";
		} else if (str.equals("3")) {
			return "Y";
		} else {
			return "";
		}
	}

	public static String getPeriodIndicatorForECC(String str) {
		if (ServicesUtil.isEmpty(str) || "D".equals(str)) {
			return null;
		} else if (str.equals("W")) {
			return "1";
		} else if (str.equals("M")) {
			return "2";
		} else if (str.equals("Y")) {
			return "3";
		} else if (str.equals("#")) { // Added for INC000102018087
			return "#";
		} else {
			return null;
		}
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static String byteArrayToHexString(byte[] processId) {
		if (ServicesUtil.isEmpty(processId))
			return null;
		final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] hexChars = new char[processId.length * 2];
		int v;
		for (int j = 0; j < processId.length; j++) {
			v = processId[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static Boolean getBooleanFromECCString(String value) {
		if (!isEmpty(value) && value.equals("X")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public static String ConvertDateToString(Date date) {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = null;
		if (date != null) {
			strDate = dateFormat.format(date);
			return strDate;

		} else {
			return strDate;
		}

	}

	public static Date ConvertStringToDate(String sDate1) {

		Date date1 = null;
		try {
			if (sDate1 != null) {

				date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
			}

		} catch (ParseException e) {

			e.printStackTrace();
		}
		return date1;
	        
	    }

}
