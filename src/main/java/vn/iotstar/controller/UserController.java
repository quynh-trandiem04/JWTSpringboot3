package vn.iostart.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

import vn.iostart.entity.User;
import vn.iostart.service.UserService;

public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/me")
	public ResponseEntity<User> authenticatedUser(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User) authentication.getPrincipal();
		return ResponseEntity.ok(currentUser);
	}
	@GetMapping("/")
	public ResponseEntity<List<User>> allUsers(){
		List<User> users = userService.allUser();
		return ResponseEntity.ok(users);
	}
}
