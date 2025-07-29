package org.example;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class Admin extends User {

    public Admin(String name, String password) {
        super( name, password, true);
    }

    public void addBook(Book book) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(book);
            tx.commit();
            System.out.println("Book saved successfully!");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void removeBook(int bookId) {
       Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try{
            tx = session.beginTransaction();
            Book book = session.get(Book.class, bookId);
            if (book != null) {
                session.delete(book);
                tx.commit();
                System.out.println("Book removed successfully!");
            } else {
                System.out.println("Book not found with ID: " + bookId);
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void updateBook(Book book) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.update(book);
            tx.commit();
            System.out.println("Book saved successfully!");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Book getBookById(int bookId){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Book book = null;
        try {
            tx = session.beginTransaction();
            book = session.get(Book.class, bookId);
            tx.commit();
        }catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return book;
    }

    public void registerUser(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try{
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
            System.out.println("User registered successfully!");

        }catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void removeUser(int userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = session.get(User.class, userId);
            if (user != null) {
                session.delete(user);
                tx.commit();
                System.out.println("User removed successfully!");
            } else {
                System.out.println("User not found with ID: " + userId);
            }

        }catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public boolean checkCredentials(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User admin = session.createQuery(
                    "FROM User WHERE name = :name AND password = :password AND isAdmin = true ",
                    User.class)
                    .setParameter("name", getName())
                    .setParameter("password", getPassword())
                    .uniqueResult();
            tx.commit();
            if (admin != null) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return false;
    }

    public List<RegularUser> viewRegUsers(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<User> users = null;
        try {
            tx = session.beginTransaction();
            users = session.createQuery("FROM User WHERE isAdmin = false", User.class).list();
            tx.commit();
        }catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        // Convert List<User> to List<RegularUser>
        List<RegularUser> regularUsers = users.stream()
                .map(user -> {
                    RegularUser regularUser = new RegularUser(user.getName(), user.getPassword());
                    regularUser.setId(user.getId());
                    return regularUser;
                })
                .toList();

        return regularUsers;
    }
    @Override
    public String toString() {
        return "Admin{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                '}';
    }

}
