package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            Dotenv dotenv = Dotenv.load();

            String dbUrl = String.format(
                    "jdbc:mysql://%s:%s/%s",
                    dotenv.get("DB_HOST"),
                    dotenv.get("DB_PORT"),
                    dotenv.get("DB_NAME")
            );

            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            configuration.setProperty("hibernate.connection.url", dbUrl);
            configuration.setProperty("hibernate.connection.username", dotenv.get("DB_USER"));
            configuration.setProperty("hibernate.connection.password", dotenv.get("DB_PASSWORD"));
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");
            configuration.setProperty("show_sql", "true");

            configuration.addAnnotatedClass(org.example.User.class);
            configuration.addAnnotatedClass(org.example.Book.class);
            configuration.addAnnotatedClass(org.example.BorrowedBook.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
