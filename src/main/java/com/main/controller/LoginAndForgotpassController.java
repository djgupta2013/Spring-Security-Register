package com.main.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.main.model.User;
import com.main.serviceImpl.EmailServiceImpl;
import com.main.serviceImpl.UserServiceImpl;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

@Controller
public class LoginAndForgotpassController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private EmailServiceImpl emailService;
	
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
		
		// Return emailVarify form template
				@RequestMapping(value="/emailVarify", method = RequestMethod.GET)
				public ModelAndView showEmailPage(ModelAndView modelAndView, User user){
					modelAndView.addObject("user", user);
					modelAndView.setViewName("emailVarify");
					return modelAndView;
				}
				
		//Process form input data
				@RequestMapping(value="/emailVarify", method=RequestMethod.POST)
				public ModelAndView proccessEmailVarify(ModelAndView modelAndView, @Valid User user,BindingResult bindingResult, HttpServletRequest request) {
					
					User userExists = userService.findByEmail(user.getEmail());
					
					if(userExists !=null && userExists.getEnabled()==true) {
						emailService.sendMail(userExists.getEmail(), userExists.getConfirmationToken(), request,"forgotPass");
						modelAndView.addObject("confirmationMessage", "mail send");
						modelAndView.setViewName("emailVarify");
					}
					else {
						modelAndView.addObject("confirmationMessage", "Your mail is not register ! Please register first");
						modelAndView.setViewName("emailVarify");
					}
					return modelAndView;
					
				}
				
				// Process confirmation link
				@RequestMapping(value="/forgotPass", method = RequestMethod.GET)
				public ModelAndView confirmRegistration(ModelAndView modelAndView, @RequestParam("token") String token) {
						
					User user = userService.findByConfirmationToken(token);
						
					if (user == null) { // No token found in DB
						modelAndView.addObject("invalidToken", "Oops!  This is an invalid confirmation link.");
					} else { // Token found
						modelAndView.addObject("confirmationToken", user.getConfirmationToken());
					}
					modelAndView.setViewName("forgotPass");
					return modelAndView;		
				}
				
				// Process confirmation link
				@RequestMapping(value="/forgotPass", method = RequestMethod.POST)
				public ModelAndView confirmRegistration(ModelAndView modelAndView, BindingResult bindingResult, @RequestParam Map<String, String> requestParams, RedirectAttributes redir) {
							
					modelAndView.setViewName("forgotPass");
					
					Zxcvbn passwordCheck = new Zxcvbn();
					
					Strength strength = passwordCheck.measure(requestParams.get("password"));
					
					if (strength.getScore() < 3) {
						//modelAndView.addObject("errorMessage", "Your password is too weak.  Choose a stronger one.");
						bindingResult.reject("password");
						
						redir.addFlashAttribute("errorMessage", "Your password is too weak.  Choose a stronger one.");

						modelAndView.setViewName("redirect:forgotPass?token=" + requestParams.get("token"));
						System.out.println(requestParams.get("token"));
						return modelAndView;
					}
					
					// Find the user associated with the reset token
					User user = userService.findByConfirmationToken(requestParams.get("token"));

					// Set new password
					user.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password")));

					// Set user to enabled
					user.setEnabled(true);
					
					// Save user
					userService.saveUser(user);
					
				    //modelAndView.addObject("successMessage", "Your password has been set! Please go to login page");
					//modelAndView.setViewName("login");
					//modelAndView.addObject("login");
					return new ModelAndView("redirect:login");	
					//return modelAndView;
				}	
		
}
