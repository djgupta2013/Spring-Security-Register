package com.main.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.model.User;
import com.main.repository.UserRepository;

@Service("userService")
@Transactional
public class UserService {
	
	private UserRepository userRepository;
	
	
	
	@Autowired
	public UserService(UserRepository userRepository) {
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
		userRepository.save(user);
	}
	
/*	public User findByEmailAndPassword(String email,String password) {
		
		User user=userRepository.findByEmail(email);
		System.out.println("search");
		System.out.println(user.getEmail());
		
		
		
		boolean s=bCryptPasswordEncoder.matches(password, user.getPassword());
		
		if(user!=null) {
			if(bCryptPasswordEncoder.matches(password, user.getPassword())) {
				System.out.println("seccuss");
				
			}else {
				System.out.println("fail");
				return user=null;
			}
		}
		
		return user;
		
	}*/

}
