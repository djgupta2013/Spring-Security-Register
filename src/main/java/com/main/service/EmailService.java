package com.main.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
	public void sendMail(SimpleMailMessage simpleMailMessage);
}
