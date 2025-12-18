package com.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.entity.Books;
import com.library.service.BookService;
@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
	
	@Mock
	private BookService bookService;
	@InjectMocks
	private BookController bookController;
	
	private MockMvc mockMvc;
	
	private Books b1;
	private Books b2;
	private ObjectMapper objectMapper;
	
	private List<Books> testBookList;
	
	@BeforeEach
	void setUpBooks()
	{
		mockMvc=MockMvcBuilders.standaloneSetup(bookController).build();
		objectMapper=new ObjectMapper();
		b1=new Books();
		b1.setBid(1);
		b1.setBname("Java");
		b1.setBauth("Gosling");
		b1.setBcategory("Technical");
		b1.setPyear("2023");
		b1.setIsbn("isbn10045");
		b1.setStatus("BORROWED");
		
		b2=new Books();
		b2.setBid(1);
		b2.setBname("Java");
		b2.setBauth("Gosling");
		b2.setBcategory("Technical");
		b2.setPyear("2023");
		b2.setIsbn("isbn10045");
		b2.setStatus("BORROWED");
		
		testBookList=Arrays.asList(b1,b2);
	}
	
	@Test
	void addBookTest() throws Exception
	{
		when(bookService.saveBook(any(Books.class))).thenReturn(b1);
		mockMvc.perform(post("/book/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(b1)))
				.andExpect(jsonPath("$.bid").value(1))
				.andExpect(jsonPath("$.bname").value("Java"))
				.andExpect(jsonPath("$.bauth").value("Gosling"))
				.andExpect(jsonPath("$.bcategory").value("Technical"))
				.andExpect(jsonPath("$.pyear").value("2023"))
				.andExpect(jsonPath("$.isbn").value("isbn10045"))
				.andExpect(jsonPath("$.status").value("BORROWED"))
				.andDo(print());
		verify(bookService).saveBook(any(Books.class));		
	}
	
}
