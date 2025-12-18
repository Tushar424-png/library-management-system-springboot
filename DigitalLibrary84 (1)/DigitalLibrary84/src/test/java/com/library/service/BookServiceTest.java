package com.library.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.library.entity.Books;
import com.library.repository.BookRepository;
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
	@Mock
	BookRepository bookRepo;
	@InjectMocks
	BookService bookService;
	@Test
	void addBookService()
	{
		Books book=new Books();
		book.setBid(1);
		book.setBname("Java");
		book.setBauth("Gosling");
		book.setBcategory("Technical");
		book.setPyear("2023");
		book.setIsbn("isbn10045");
		book.setStatus("BORROWED");
		Mockito.when(bookRepo.save(book)).thenReturn(book);
		Books theBook=bookService.saveBook(book);
		
		//Testing Book==Matched Book
		
		Assertions.assertEquals(book.getBname(), theBook.getBname());
	}
}
