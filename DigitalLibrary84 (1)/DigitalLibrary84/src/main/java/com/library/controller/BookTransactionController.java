package com.library.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.entity.BookTransaction;
import com.library.entity.Books;
import com.library.entity.User;
import com.library.repository.BookRepository;
import com.library.repository.UserRepository;
import com.library.service.BookTransactionService;
import com.library.service.UserService;

@RestController
@RequestMapping("/transaction")
public class BookTransactionController {

    @Autowired
    private BookTransactionService transactionService;

 
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BookRepository bookRepo;

    // Borrow a book → only the logged-in user can borrow
    @PostMapping("borrow/{userId}/{bookId}")
    public ResponseEntity<?> borrowBook(@PathVariable int userId, @PathVariable int bookId) {
        Optional<User> user = userRepo.findById(userId);
        Optional<Books> book = bookRepo.findById(bookId);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("User not found with ID: " + userId);
        }

        if (book.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Book not found with ID: " + bookId);
        }

        // call service to borrow
        transactionService.borrowBook(userId,bookId);

        return ResponseEntity.ok("Book borrowed successfully");
    }


    @PutMapping("/return/{transactionId}")
    public ResponseEntity<BookTransaction> returnBook(@PathVariable int transactionId) {
        try {
            BookTransaction transaction = transactionService.returnBook(transactionId);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/return/{userId}/{bookId}")
    public ResponseEntity<?> returnBook(@PathVariable int userId, @PathVariable int bookId) {
        try {
            BookTransaction transaction = transactionService.returnBook(userId, bookId);

            if (transaction.getFineAmount() > 0) {
                return ResponseEntity.ok("Book is returned successfully. Pending fine: ₹" + transaction.getFineAmount());
            } else {
                return ResponseEntity.ok("Book is returned successfully.");
            }

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/getall")
    public List<BookTransaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/user/{userId}")
    public List<BookTransaction> getUserTransactions(@PathVariable int userId) {
        return transactionService.getUserTransactions(userId);
    }

    @GetMapping("/user/{userId}/current")
    public List<BookTransaction> getUserCurrentBorrowedBooks(@PathVariable int userId) {
        return transactionService.getUserCurrentBorrowedBooks(userId);
    }

    @GetMapping("/book/{bookId}")
    public List<BookTransaction> getBookTransactions(@PathVariable int bookId) {
        return transactionService.getBookTransactions(bookId);
    }

    @GetMapping("/current-borrowed")
    public List<BookTransaction> getCurrentBorrowedBooks() {
        return transactionService.getCurrentBorrowedBooks();
    }

    @GetMapping("/overdue")
    public List<BookTransaction> getOverdueBooks() {
        return transactionService.getOverdueBooks();
    }
}
