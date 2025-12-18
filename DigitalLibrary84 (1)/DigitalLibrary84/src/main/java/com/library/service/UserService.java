package com.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.library.entity.Books;
import com.library.entity.User;
import com.library.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	private BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder(12);
	public User saveUser(User u) {
		u.setPassword(passwordEncoder.encode(u.getPassword()));
		return userRepository.save(u);
	}
	// Get All User

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public Optional<User> getUserById(int userId) {
		return userRepository.findById(userId);
	}

	public User updateUser(User u, int id) {
		Optional<User> theUser = userRepository.findById(id);

		User newUser = theUser.get();
		newUser.setUserName(u.getUserName());
		newUser.setEmail(u.getEmail());
		newUser.setPhone(u.getPhone());
		newUser.setRegistrationDate(u.getRegistrationDate());
		newUser.setAddress(u.getAddress());

		return userRepository.save(newUser);
	}

	public void deleteuser(int id) {
		userRepository.deleteById(id);
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	public Optional<User> findByUserName(String username) {
		return userRepository.findOneByUserName(username);
	}

	
	// Get active users
    public List<User> getActiveUsers() {
        return userRepository.findActiveUsers();
    }
    
    // Get users by status
    public List<User> getUsersByStatus(String status) {
        return userRepository.findByStatus(status);
    }

	// Suspend a user

	public User suspendUser(int userId) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			User suspendUser = user.get();
			suspendUser.setStatus("SUSPENDED");
			return userRepository.save(suspendUser);
		}
		return null;
	}
	// Activate a user

	public User activateUser(int userId) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			User suspendUser = user.get();
			suspendUser.setStatus("ACTIVE");
			return userRepository.save(suspendUser);
		}
		return null;
	}

	// Get total active users count
	public long getActiveUsersCount() {
		return userRepository.countActiveUsers();
	}


}
