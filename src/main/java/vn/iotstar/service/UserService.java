package vn.iostart.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;

import vn.iostart.entity.User;
import vn.iostart.repositpry.UserRepository;

public class UserService {
	private final UserRepository userRepository;
		
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	public List<User> allUser(){
		List<User> users = new ArrayList<>();
		
		userRepository.findAll().forEach(users::add);
		return users;
	}
	
}
