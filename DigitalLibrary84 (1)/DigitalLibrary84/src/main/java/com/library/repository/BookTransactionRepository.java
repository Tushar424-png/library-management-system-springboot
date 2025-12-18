package com.library.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.library.entity.BookTransaction;

@Repository
public interface BookTransactionRepository extends JpaRepository<BookTransaction, Integer> {

    List<BookTransaction> findByUserUserId(int userId);
    List<BookTransaction> findByBookBid(int bookId);
    List<BookTransaction> findByStatus(BookTransaction.TransactionStatus status);

    @Query("SELECT bt FROM BookTransaction bt WHERE bt.status = 'BORROWED'")
    List<BookTransaction> findCurrentBorrowedBooks();

    @Query("SELECT bt FROM BookTransaction bt WHERE bt.dueDate < :currentDate AND bt.status = 'BORROWED'")
    List<BookTransaction> findOverdueBooks(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT bt FROM BookTransaction bt WHERE bt.user.userId = :userId AND bt.status = 'BORROWED'")
    List<BookTransaction> findUserCurrentBorrowedBooks(@Param("userId") int userId);

    @Query("SELECT COUNT(bt) FROM BookTransaction bt WHERE bt.user.userId = :userId AND bt.status = 'BORROWED'")
    long countUserBorrowedBooks(@Param("userId") int userId);

    @Query("SELECT bt FROM BookTransaction bt WHERE bt.book.bid = :bookId AND bt.status = 'BORROWED'")
    BookTransaction findCurrentBorrowedBookTransaction(@Param("bookId") int bookId);

    // ✅ Fixed nested fields
    boolean existsByUserUserIdAndBookBidAndReturnDateIsNull(int userId, int bookId);
    
    @Query("SELECT bt FROM BookTransaction bt WHERE bt.user.userId = :userId AND bt.book.bid = :bookId AND bt.status = 'BORROWED'")
    Optional<BookTransaction> findActiveTransaction(@Param("userId") int userId, @Param("bookId") int bookId);

}
