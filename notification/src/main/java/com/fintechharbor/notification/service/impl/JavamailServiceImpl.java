package com.fintechharbor.notification.service.impl;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.fintechharbor.notification.service.JavamailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JavamailServiceImpl implements JavamailService {

	private final JavaMailSender mailSender;

	public void sendEmail(String recipientEmail, String subject, Integer otp) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo(recipientEmail);
		helper.setSubject(subject);
		String emailBody = String.format("Your 6 digit Otp for registration on fintechharbour is : %s", otp);
		helper.setText(emailBody, true);
		mailSender.send(message);
	}

}
