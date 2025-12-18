package com.library.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.entity.User;
import com.library.repository.UserRepository;
import com.library.service.Jwtservice;
import com.library.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	
	@Autowired
	private UserRepository userRepo;
	 
	@Autowired
	private Jwtservice jwtservice;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private UserService userservice;
	
	@PostMapping("/add")
	public ResponseEntity<?> registerUser(@RequestBody User user) {

	    // ✅ Check if email already exists
	    if (userRepo.existsByEmail(user.getEmail())) {
	        return ResponseEntity
	                .badRequest()
	                .body(Map.of("error", "Email already registered. Please use a different email."));
	    }

	    // ✅ Check if username already exists
	    if (userRepo.existsByUserName(user.getUserName())) {
	        String base = user.getUserName();
	        int counter = 1;
	        String newName = base + counter;

	        while (userRepo.existsByUserName(newName)) {
	            counter++;
	            newName = base + counter;
	        }

	        user.setUserName(newName); // auto-adjust username
	    }

	    // ✅ Save using UserService (ensures password encoding)
	    User savedUser = userService.saveUser(user);

	    return ResponseEntity.ok(Map.of(
	            "message", "User registered successfully",
	            "user", savedUser
	    ));
	}
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {

	    String username = loginData.get("username");
	    String password = loginData.get("password");

	    // ✅ Find user (Optional)
	    Optional<User> userOpt = userRepo.findOneByUserName(username);

	    if (userOpt.isEmpty()) {
	        return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
	    }

	    User user = userOpt.get();  // ✅ Extract real user

	    // ✅ Password check
	    if (!encoder.matches(password, user.getPassword())) {
	        return ResponseEntity.badRequest().body(Map.of("error", "Invalid password"));
	    }

	    // ✅ Token generate
	    String token = jwtservice.genToken(user.getUserName());

	    return ResponseEntity.ok(
	            Map.of(
	                "message", "Login Success",
	                "token", token,
	                "user", user
	            )
	    );
	}

	@GetMapping("/getall")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/getone/{id}")
	public ResponseEntity<User> getUserById(@PathVariable int id) {
		Optional<User> user = userService.getUserById(id);
		return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable int id) {
		User updatedUser = userService.updateUser(user, id);
		if (updatedUser != null) {
			return ResponseEntity.ok(updatedUser);
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable int id) {
		try {
			userService.deleteuser(id);
			return ResponseEntity.ok("User deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error deleting user");
		}
	}

	@GetMapping("/search/email/{email}")
	public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
		Optional<User> user = userService.findByEmail(email);
		return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/search/name/{name}")
	public Optional<User> searchUsersByName(@PathVariable String name) {
		return userService.findByUserName(name);
	}

	@GetMapping("/active")
	public List<User> getActiveUsers() {
		return userService.getActiveUsers();
	}

	@GetMapping("/status/{status}")
	public List<User> getUsersByStatus(@PathVariable String status) {
		return userService.getUsersByStatus(status);
	}

	@PutMapping("/suspend/{id}")
	public ResponseEntity<User> suspendUser(@PathVariable int id) {
		User suspendedUser = userService.suspendUser(id);
		if (suspendedUser != null) {
			return ResponseEntity.ok(suspendedUser);
		}
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/activate/{id}")
	public ResponseEntity<User> activateUser(@PathVariable int id) {
		User activatedUser = userService.activateUser(id);
		if (activatedUser != null) {
			return ResponseEntity.ok(activatedUser);
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/count/active")
	public ResponseEntity<Long> getActiveUsersCount() {
		long count = userService.getActiveUsersCount();
		return ResponseEntity.ok(count);
	}

}
