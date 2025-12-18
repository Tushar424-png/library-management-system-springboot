package com.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DigitalLibrary84Application {

	public static void main(String[] args) {
		SpringApplication.run(DigitalLibrary84Application.class, args);
	}

}

/*
 * Book Management
 * 1. CRUD OPerations(Insert, Update, Delete and Search)
 * 2. Search Operations by Book Name, Book Author, Category, ISBN
 * 3. Filter Operations
 * 
 * User Management
 * 1. Register new User
 * 2. User Profile Management
 * 3. Activate/Suspend user
 * 4. Search User
 * 
 * Book Borrowing System
 * 1. Borrow a book with due dates
 * 2. Return a Book with fine calculation
 * 3. Extend the due dates
 * 4. Overdue charges tracking
 * 5. Tracking lost book
 * 6. Borrowing Limit(5)
 * 
 * */
