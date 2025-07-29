package org.example;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id // Unique identifier for the book (pk)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate the ID
    private int id;
    private String title;
    private String author;
    private String genre;
    private int availableCopies;

    Book() {
        // Default constructor for Hibernate ?? why used
    }
    Book(String title, String author, String genre, int availableCopies) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.availableCopies = availableCopies;
    }
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getGenre() {
        return genre;
    }
    public int getAvailableCopies() {
        return availableCopies;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public static Book findBookById(int id){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Book book = null;

        try{
            tx = session.beginTransaction();
            book = session.get(Book.class, id);
        }catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return book;
    }

    public static Book findBookByTitle(String title){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Book book = null;

        try{
            tx = session.beginTransaction();
            book = session.createQuery("FROM Book WHERE title = :title", Book.class)
                          .setParameter("title", title)
                          .uniqueResult();
        }catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return book;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", availableCopies=" + availableCopies +
                '}';
    }
}
