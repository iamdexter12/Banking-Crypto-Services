package com.fintechharbor.notification.service;

import jakarta.mail.MessagingException;

public interface JavamailService {
	
	public void sendEmail(String recipientEmail, String subject, Integer generatedEmailOtp) throws MessagingException;

}
