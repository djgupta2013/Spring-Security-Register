package com.main.serviceImpl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.model.User;
import com.main.service.UserService;

@Service("userService")
public class UserServiceImpl {
	private UserService userRepository;
	
	@Autowired
	public UserServiceImpl(UserService userRepository) {
		this.userRepository=userRepository;
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public void deleteByEmail1(String email) {
		 userRepository.deleteUserByEmail(email);
	}
	
	public User findByConfirmationToken(String confirmationToken) {
		return userRepository.findByConfirmationToken(confirmationToken);
	}
	
	public void saveUser(User user) {
		
		 // Generate random 36-character string token for confirmation link
	    user.setConfirmationToken(UUID.randomUUID().toString());
		userRepository.save(user);
	}
}




























