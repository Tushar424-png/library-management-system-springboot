package com.library.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.library.entity.Books;
import com.library.entity.User;
import com.library.repository.BookRepository;
import com.library.service.BookService;
import com.library.service.BookTransactionService;
import com.library.service.UserService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookTransactionService bookTransactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Books> uploadBook(
            @RequestPart("file") MultipartFile file,
            @RequestPart("book") Books bookRequest) {
        try {

            // ⭐ Save to Desktop location
            String uploadDir = "C:/Users/Tushar handa/Desktop/folder/";

            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            // Save only filename
            String filename = file.getOriginalFilename();
            String filePath = uploadDir + filename;

            // Save file to folder
            file.transferTo(new File(filePath));

            // Save only name in DB
            bookRequest.setFileUrl(filename);

            Books savedBook = bookService.saveBook(bookRequest);
            return ResponseEntity.ok(savedBook);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/view/{id}")
    public ResponseEntity<Resource> viewFile(
            @PathVariable int id,
            Authentication authentication) throws IOException {

        Books book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        String currentUserEmail = authentication.getName();
        User currentUser = userService.findByUserName(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            boolean allowed = bookTransactionService.isBorrowedByUser(book.getBid(), currentUser.getUserId());
            if (!allowed) {
                return ResponseEntity.status(403).build();
            }
        }

        // ⭐ SAME DESKTOP PATH
        String basePath = "C:/Users/Tushar handa/Desktop/folder/";
        File file = new File(basePath + book.getFileUrl());

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        UrlResource resource = new UrlResource(file.toURI());
        String mime = Files.probeContentType(file.toPath());
        if (mime == null) mime = "application/pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mime))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                .body(resource);
    }
    @PostMapping("/add")
    public ResponseEntity<Books> addBook(@RequestBody Books b) {
        try {
            Books savedBook = bookService.saveBook(b);
            return ResponseEntity.ok(savedBook);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getall")
    public List<Books> getAll() {
        return bookService.getAll();
    }

    @GetMapping("/getone/{id}")
    public ResponseEntity<Books> getOne(@PathVariable int id) {
        Optional<Books> book = bookService.getOne(id);
        return book.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Books> updateRecord(@RequestBody Books b, @PathVariable int id) {
        Books updatedBook = bookService.updateBook(b, id);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable int id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok("Book deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting book");
        }
    }

    @GetMapping("/search/name/{bookName}")
    public List<Books> searchBooksByName(@PathVariable String bookName) {
        return bookService.searchBooksByName(bookName);
    }

    @GetMapping("/search/author/{author}")
    public List<Books> searchBooksByAuthor(@PathVariable String author) {
        return bookService.searchBooksByAuthor(author);
    }

    @GetMapping("/search/category/{category}")
    public List<Books> searchBooksByCategory(@PathVariable String category) {
        return bookService.searchBooksByCategory(category);
    }

    @GetMapping("/status/{status}")
    public List<Books> getBooksByStatus(@PathVariable String status) {
        return bookService.getBooksByStatus(status);
    }

    @GetMapping("/available")
    public List<Books> getAvailableBooks() {
        return bookService.getAvailableBooks();
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Books> getBookByIsbn(@PathVariable String isbn) {
        Books book = bookService.getBookByIsbn(isbn);
        if (book != null) {
            return ResponseEntity.ok(book);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/year/{year}")
    public List<Books> getBooksByYear(@PathVariable String year) {
        return bookService.getBooksByYear(year);
    }

    @GetMapping("/search")
    public List<Books> searchBooks(
            @RequestParam(required = false) String bookName,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category) {
        return bookService.searchBooks(bookName, author, category);
    }

    // Statistics endpoints
    @GetMapping("/count/total")
    public ResponseEntity<Long> getTotalBooks() {
        long count = bookService.getTotalBooks();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/available")
    public ResponseEntity<Long> getAvailableBooksCount() {
        long count = bookService.getAvailableBooksCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/borrowed")
    public ResponseEntity<Long> getBorrowedBooksCount() {
        long count = bookService.getBorrowedBooksCount();
        return ResponseEntity.ok(count);
    }
}
