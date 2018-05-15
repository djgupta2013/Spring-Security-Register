package com.main.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.main.model.User;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Long> {
     User findByEmail(String email);
     User findByConfirmationToken(String confirmationToken);
     User findByEmailAndPassword(String email,String password);
	void deleteUserByEmail(String email);
}