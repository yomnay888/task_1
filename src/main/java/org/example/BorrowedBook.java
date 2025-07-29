package org.example;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "borrowed_books")
public class BorrowedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(name = "is_returned")
    private boolean isReturned;

    public BorrowedBook(User user, Book book, boolean isReturned) {
        this.user = user;
        this.book = book;
        this.isReturned = isReturned;
    }
    public BorrowedBook() {
        // Default constructor for Hibernate
    }
    public int getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public Book getBook() {
        return book;
    }
    public boolean isReturned() {
        return isReturned;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public void setReturned(boolean returned) {
        isReturned = returned;
    }
    @Override
    public String toString() {
        return "BorrowedBook{" +
                "id=" + id +
                ", user=" + user.getName() +
                ", book=" + book.getTitle() +
                ", isReturned=" + isReturned +
                '}';
    }
}
