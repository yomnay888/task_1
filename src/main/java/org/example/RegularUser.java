package org.example;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class RegularUser extends User implements Borrowable {

    public RegularUser(String name, String password) {
        super(name, password, false);
    }

    @Override
    public void borrowBook(int bookId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try{
            tx = session.beginTransaction();
            Book book = session.get(Book.class, bookId);

            // check if book exists
            if (book == null) {
                System.out.println("Book not found with ID: " + bookId);
                return;
            }
            // check if user has already borrowed this book
            BorrowedBook borrowedBook = session.createQuery(
                    "FROM BorrowedBook  WHERE user.id = :userId AND book.id = :bookId AND isReturned = false",
                    BorrowedBook.class)
                    .setParameter("userId", getId())
                    .setParameter("bookId", bookId)
                    .uniqueResult();

            if(borrowedBook != null) {
                System.out.println("You have already borrowed this book.");
            }
            // check if book is available
            else if (book.getAvailableCopies() != 0) {
                book.setAvailableCopies(book.getAvailableCopies() - 1);
                BorrowedBook newBorrowedBook = new BorrowedBook(this, book, false);
                session.save(newBorrowedBook);
                session.update(book);
                tx.commit();
                System.out.println("Book borrowed successfully!");
            } else {
                System.out.println("Book not available for borrowing.");
            }

        }catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void returnBook(int bookId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try{
            tx = session.beginTransaction();
            BorrowedBook borrowedBook = session.createQuery(
                    "FROM BorrowedBook WHERE user.id = :userId AND book.id = :bookId AND isReturned = false",
                    BorrowedBook.class)
                    .setParameter("userId", getId())
                    .setParameter("bookId", bookId)
                    .uniqueResult();

            if (borrowedBook != null) {
                Book book = borrowedBook.getBook();
                book.setAvailableCopies(book.getAvailableCopies() + 1);
                borrowedBook.setReturned(true);
                session.update(book);
                session.update(borrowedBook);
                tx.commit();
                System.out.println("Book returned successfully!");
            } else {
                System.out.println("You have not borrowed this book or it has already been returned.");
            }

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public RegularUser checkCredentials(){
        User user = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            user = session.createQuery(
                    "FROM User WHERE name = :name AND password = :password AND isAdmin = false",
                    User.class)
                    .setParameter("name", getName())
                    .setParameter("password", getPassword())
                    .uniqueResult();
        }catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        RegularUser regUser = null;
        if (user != null) {
            regUser = new RegularUser(user.getName(), user.getPassword());
            regUser.setId(user.getId());
        }
        return regUser;
    }
    @Override
    public String toString() {
        return "RegularUser{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +

                '}';
    }
}
