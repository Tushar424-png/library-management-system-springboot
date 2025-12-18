package com.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.library.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);
	Optional<User> findOneByUserName(String userName);

	List<User> findByStatus(String status);

	@Query("SELECT u FROM User u WHERE u.status = 'ACTIVE'")
	List<User> findActiveUsers();

	@Query("SELECT COUNT(u) FROM User u WHERE u.status = 'ACTIVE'")
	long countActiveUsers();
    
	boolean existsByUserName(String userName);
	 boolean existsByEmail(String email);
}
