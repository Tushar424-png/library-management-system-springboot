package com.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.library.entity.Books;

@Repository
public interface BookRepository extends JpaRepository<Books, Integer> {
	
	//Search Operations
	List<Books> findByBname(String bname);
	List<Books> findByBauth(String bauth);
	List<Books> findByBcategory(String bcategory);
	
	List<Books> findByStatus(String status);
	
	@Query("SELECT b FROM Books b WHERE b.status = 'AVAILABLE'")
    List<Books> findAvailableBooks();
	
	//Find Book By Publication Year
	
	List<Books> findByPyear(String pyear);
	// Search books by ISBN
    Books findByIsbn(String isbn);
 // Count books by status
    @Query("SELECT COUNT(b) FROM Books b WHERE b.status = :status")
    long countBooksByStatus(@Param("status") String status);
	
    
 // Combined search
    @Query("SELECT b FROM Books b WHERE " +
           "(:bookName IS NULL OR LOWER(b.bname) LIKE LOWER(CONCAT('%', :bookName, '%'))) AND " +
           "(:author IS NULL OR LOWER(b.bauth) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
           "(:category IS NULL OR LOWER(b.bcategory) LIKE LOWER(CONCAT('%', :category, '%')))")
    List<Books> searchBooks(@Param("bookName") String bookName, 
                           @Param("author") String author, 
                           @Param("category") String category);
}
