package com.gabriel.course.projectapi2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabriel.course.projectapi2.model.User;
import com.gabriel.course.projectapi2.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Transactional(readOnly = true)
	public List<User> findUsers() {
		return userRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public User findById(Long id) {
		return userRepository.findById(id).orElseThrow(
				() -> new RuntimeException("Not found user by id: " + id) );
	}
	
	@Transactional
	public User createUser(User user) {
		return userRepository.save(user);
	}
	
	@Transactional
	public User updatePassword(Long id, String currentPass, String newPass, String confirmPass) {
			
		var oldUser = userRepository.getReferenceById(id);
		
		if(!currentPass.equals(oldUser.getPassword())) {
			throw new RuntimeException("Digite a senha atual correta!");
		}
		
		if(!newPass.equals(confirmPass)) {
			throw new RuntimeException("Senhas incompativeis!");			
		}
		
		return userRepository.save(modPasswordUser(oldUser, newPass));
	}
	
	private User modPasswordUser(User u1, String newPass) {
		u1.setPassword(newPass);
		
		return u1;
	}
}
