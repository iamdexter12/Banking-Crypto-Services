package com.spring.userservice.service;

import jakarta.mail.MessagingException;

public interface JavamailService {

	void sendNotificationForDocument(String email) throws MessagingException;

	public void sendEmail(String recipientEmail, String subject, Integer generatedEmailOtp, String email) throws MessagingException;

}
