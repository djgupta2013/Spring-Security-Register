package com.main.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.main.model.User;
import com.main.service.UserService;

@Controller
public class LoginController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserService userService;
	
	
	
	// Return login form template
		@RequestMapping(value="/login", method = RequestMethod.GET)
		public ModelAndView showRegistrationPage(ModelAndView modelAndView, User user){
			modelAndView.addObject("user", user);
			modelAndView.setViewName("login");
			return modelAndView;
		}
		
		// Process form input data
		@RequestMapping(value = "/login", method = RequestMethod.POST)
		public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid User user, BindingResult bindingResult, HttpServletRequest request) {
					
			// Lookup user in database by e-mail
			User userExists = userService.findByEmail(user.getEmail());
			
			if (userExists == null || userExists.getEnabled() !=true) {
				modelAndView.addObject("invalidEmail", "Oops!  this "+user.getEmail()+" email is not register.");
				modelAndView.setViewName("login");
				//bindingResult.reject("email");
			}
			/*if(userExists.getEnabled() !=true) {
			System.out.println("hello");	
			modelAndView.addObject("invalidEmail", "Oops!  this "+user.getEmail()+" email is not register.");
			modelAndView.setViewName("login");
			}*/
				
			 else {
				if(bCryptPasswordEncoder.matches(user.getPassword(), userExists.getPassword())) {
					System.out.println("seccuss");
					modelAndView.setViewName("success");
				}else {
					modelAndView.addObject("invalidEmailAndPassword", "Oops!  please enter correct email and password");
					modelAndView.setViewName("login");
					bindingResult.reject("email");
				}
				
				
			}
			
			return modelAndView;
			
			
		}

}
