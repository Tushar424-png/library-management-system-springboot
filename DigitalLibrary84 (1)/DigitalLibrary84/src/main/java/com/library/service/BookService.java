package com.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.entity.BookTransaction;
import com.library.entity.Books;
import com.library.repository.BookRepository;

@Service
public class BookService {
	@Autowired
	private BookRepository bookRepo;
	@Autowired
	private BookTransactionService transactionService;
	
	public Books saveBook(Books b)
	{
		// Set default status if not provided
        if (b.getStatus() == null || b.getStatus().isEmpty()) {
            b.setStatus("AVAILABLE");
        }
		return bookRepo.save(b);
	}
	
	public List<Books> getAll()
	{
		return bookRepo.findAll();
	}
	
	public Optional<Books> getOne(int bid)
	{
		return bookRepo.findById(bid);
	}
	
	public Books updateBook(Books b, int id)
	{
		Optional<Books> theBook=bookRepo.findById(id);
		
		Books newBook=theBook.get();
		newBook.setBname(b.getBname());
		newBook.setBauth(b.getBauth());
		newBook.setPyear(b.getPyear());
		newBook.setBcategory(b.getBcategory());
		newBook.setIsbn(b.getIsbn());
		// Only update status if provided
        if (b.getStatus() != null && !b.getStatus().isEmpty()) {
            newBook.setStatus(b.getStatus());
        }		
		return bookRepo.save(newBook);
	}
	
	public void deleteBook(int id)
	{
		bookRepo.deleteById(id);
	}
	// New search methods
    public List<Books> searchBooksByName(String bookName) {
        return bookRepo.findByBname(bookName);
    }
    
    public List<Books> searchBooksByAuthor(String author) {
        return bookRepo.findByBauth(author);
    }
    
    public List<Books> searchBooksByCategory(String category) {
        return bookRepo.findByBcategory(category);
    }
    
    public List<Books> getBooksByStatus(String status) {
        return bookRepo.findByStatus(status);
    }
    
    public List<Books> getAvailableBooks() {
        return bookRepo.findAvailableBooks();
    }
    
    public Books getBookByIsbn(String isbn) {
        return bookRepo.findByIsbn(isbn);
    }
    
    public List<Books> getBooksByYear(String year) {
        return bookRepo.findByPyear(year);
    }
    
    public List<Books> searchBooks(String bookName, String author, String category) {
        return bookRepo.searchBooks(bookName, author, category);
    }
    
    // Statistics methods
    public long getTotalBooks() {
        return bookRepo.count();
    }
    
    public long getAvailableBooksCount() {
        return bookRepo.countBooksByStatus("AVAILABLE");
    }
    
    public long getBorrowedBooksCount() {
        return bookRepo.countBooksByStatus("BORROWED");
    }
    public boolean isBookBorrowedByUser(int bookId, int userId) {
        return transactionService.isBorrowedByUser(bookId, userId);
    }


}
