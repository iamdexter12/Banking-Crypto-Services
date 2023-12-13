package com.fintechharbor.notification.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionConstant {

	public static final String FIELD_EMAIL = "email";
	public static final String FIELD_OTP = "otp";
	public static final String MESSAGE_NOT_FOUND = "Not found";
	public static final String MESSAGE_ACCOUNT_BLOCKED = "Account Blocked";
	public static final String MESSAGE_EXPIRED_OTP = "Expired Otp";
	public static final String MESSAGE_INCORRECT_OTP = "Incorrect Otp !!";
	public static final String MESSAGE_ATTEMPTS_REMAINING = " attempts remaining";
	public static final String MESSAGE_ACCOUNT_DEACTIVATED = "Account Deactivated";
	public static final String MESSAGE_SENDING_OTP_ERROR = "Exception occurred during sending OTP";
	public static final String MESSAGE_WAIT_FOR_OTP = "Otp already generated wait for 30 sec";

}
