package com.spring.userservice.service.impl;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.spring.userservice.service.JavamailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JavamailServiceImpl implements JavamailService {

	private final JavaMailSender mailSender;

	public void sendEmail(String recipientEmail, String subject, Integer otp, String email)
			throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo(recipientEmail);
		helper.setSubject(subject);
		String emailBody = String.format(
				"Click the following link to verify your email: %s/user/verify?email=%s%nYour OTP: %s",
				"http://localhost:4046", email, otp);
		helper.setText(emailBody, true);
		mailSender.send(message);
	}

	public void sendNotificationForDocument(String email) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo(email);
//		helper.setSubject(OnboardingConstant.DOCUMENT_NOTIFICATION_EMAIL);
//		String uploadDocumentLink = OnboardingConstant.DOCUMENT_UPLOAD_LINK;
		String emailContent = "Please upload your certificate of incorporation and document signed by the director at the following link";
		helper.setText(emailContent, true);

		mailSender.send(message);
	}

}
