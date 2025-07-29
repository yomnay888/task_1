package org.example;

import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id // Unique identifier for the user (pk)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate the ID
    private int id;
    private String name;
    private String password;
    private boolean isAdmin;

    public User(String name, String password, boolean isAdmin) {
        this.name = name;
        this.password = password;
        this.isAdmin = isAdmin;
    }
    public User() {
        // Default constructor for Hibernate
    }


    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public boolean isAdmin() {
        return isAdmin;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public List<Book> viewCatalog() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Book> books = null;

        try{
            tx = session.beginTransaction();
            books = session.createQuery("FROM Book", Book.class).list(); //select all rows from the Book entity
            tx.commit();
            System.out.println("Catalog retrieved successfully!");
        }  catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return books;
    }

    public <T> Book searchBooks(T input) {
        Book book = null;
        if (input.getClass() == String.class) {
            String title = (String) input;
            book = Book.findBookByTitle(title);
        } else if (input.getClass() == Integer.class) {
            int id = (Integer) input;
            book = Book.findBookById(id);
        }
        return book;
    } // search by anything with one parameter

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
