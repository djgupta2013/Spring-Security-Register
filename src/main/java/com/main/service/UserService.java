package com.main.service;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.main.model.User;

@Repository("userRepository")
public interface UserService extends CrudRepository<User, Long> {
     User findByEmail(String email);
     User findByConfirmationToken(String confirmationToken);
     User findByEmailAndPassword(String email,String password);
     @Modifying
     int deleteUserByEmail(String email);
}