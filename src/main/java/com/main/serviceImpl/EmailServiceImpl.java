package com.main.serviceImpl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailServiceImpl {
	
	private JavaMailSender mailSender;
	
	@Autowired
	public EmailServiceImpl(JavaMailSender mailSender) {
		this.mailSender=mailSender;
		
	}
	
	@Async
	public void sendMail(String  email,String confirmationToken,HttpServletRequest request,String pageName) {
		String appUrl = request.getScheme() + "://" + request.getServerName()+":8080";
		
		SimpleMailMessage registrationEmail = new SimpleMailMessage();
		registrationEmail.setTo(email);
		registrationEmail.setSubject("Registration Confirmation");
		registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
				+ appUrl + "/dhananjay/"+pageName+"?token=" + confirmationToken);
		registrationEmail.setFrom("noreply@domain.com");
 		mailSender.send(registrationEmail);
	}
}
