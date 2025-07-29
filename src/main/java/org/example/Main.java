package org.example;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        startMenu();
    }

    private static void adminMenu(Admin admin) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Admin Menu:");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. Update Book");
            System.out.println("4. View All Books");
            System.out.println("5. Register Regular User");
            System.out.println("6. Remove Regular User");
            System.out.println("7. View All Users");
            System.out.println("8. Search Books");
            System.out.println("9. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character left by nextInt()
            switch (choice) {
                case 1:
                    System.out.println("Enter Book Title:");
                    String title = scanner.nextLine();
                    System.out.println("Enter Book Author:");
                    String author = scanner.nextLine();
                    System.out.println("Enter Book Genre:");
                    String genre = scanner.nextLine();
                    System.out.println("Enter Available Copies:");
                    int availableCopies = scanner.nextInt();
                    scanner.nextLine();
                    Book book = new Book(title, author, genre, availableCopies);
                    admin.addBook(book);
                    break;
                case 2:
                    System.out.println("Enter Book ID to remove:");
                    int bookId = scanner.nextInt();
                    scanner.nextLine();
                    admin.removeBook(bookId);
                    break;
                case 3:
                    System.out.println("Enter Book ID of the Book you want to update:");
                    bookId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter New Book Title or click Enter to skip:");
                    String newTitle = scanner.nextLine();
                    System.out.println("Enter New Book Author or click Enter to skip:");
                    String newAuthor = scanner.nextLine();
                    System.out.println("Enter New Book Genre or click Enter to skip:");
                    String newGenre = scanner.nextLine();
                    System.out.println("Enter New Available Copies or press 'Enter' to skip:");
                    String copiesInput = scanner.nextLine();  // read as string to allow empty input
                    Integer newAvailableCopies = null;
                    if (!copiesInput.isEmpty()) {
                        try {
                            newAvailableCopies = Integer.parseInt(copiesInput);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number format. Skipping update for available copies.");
                        }
                    }
                    Book updatedBook = admin.getBookById(bookId);
                    if (updatedBook != null) {
                        if (!newTitle.isEmpty()) updatedBook.setTitle(newTitle);
                        if (!newAuthor.isEmpty()) updatedBook.setAuthor(newAuthor);
                        if (!newGenre.isEmpty()) updatedBook.setGenre(newGenre);
                        if (newAvailableCopies != null) updatedBook.setAvailableCopies(newAvailableCopies);
                        admin.updateBook(updatedBook);
                    } else {
                        System.out.println("Book not found with ID: " + bookId);
                    }

                    break;
                case 4:
                    System.out.println("All Books:");
                    admin.viewCatalog().forEach(System.out::println);
                    break;
                case 5:
                    System.out.println("Enter User Name:");
                    String userName = scanner.nextLine();
                    System.out.println("Enter User Password:");
                    String userPassword = scanner.nextLine();
                    User newUser = new User(userName, userPassword, false); // Regular user
                    admin.registerUser(newUser);
                    break;
                case 6:
                    System.out.println("Enter User ID to remove:");
                    int userId = scanner.nextInt();
                    scanner.nextLine();
                    admin.removeUser(userId);
                    break;
                case 7:
                    System.out.println("All Users:");
                    admin.viewRegUsers().forEach(System.out::println);
                    break;
                case 8:
                    searchBooks(admin);
                    break;
                case 9:
                    return; // Exit admin menu
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void regularUserMenu(RegularUser user) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Regular User Menu:");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. View Catalog");
            System.out.println("4. Search Books");
            System.out.println("5. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Enter Book ID to borrow:");
                    int bookId = scanner.nextInt();
                    scanner.nextLine();
                    user.borrowBook(bookId);
                    break;
                case 2:
                    System.out.println("Enter Book ID to return:");
                    bookId = scanner.nextInt();
                    scanner.nextLine();
                    user.returnBook(bookId);
                    break;
                case 3:
                    System.out.println("Catalog:");
                    user.viewCatalog().forEach(System.out::println);
                    break;
                case 4:
                    searchBooks(user);
                    break;
                case 5:
                    return; // Exit user menu
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void startMenu(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Library Management System");
        System.out.println("Please select an option:");
        System.out.println("1. Admin");
        System.out.println("2. Regular User");
        System.out.println("3. Exit");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice){
            case 1:
                // Admin login
                System.out.println("Enter Admin Name:");
                String adminName = scanner.nextLine();
                System.out.println("Enter Admin Password:");
                String adminPassword = scanner.nextLine();
                Admin admin = new Admin(adminName, adminPassword);
                if (!admin.checkCredentials()) {
                    System.out.println("Invalid Admin credentials. Exiting...");
                    return; // Exit if credentials are invalid
                }
                adminMenu(admin);
                break;

            case 2:
                // Regular User login
                System.out.println("Enter User Name:");
                String userName = scanner.nextLine();
                System.out.println("Enter User Password:");
                String userPassword = scanner.nextLine();
                RegularUser user = new RegularUser(userName, userPassword);
                user = user.checkCredentials();
                if (user == null) {
                    System.out.println("Invalid User credentials. Exiting...");
                    return; // Exit if credentials are invalid
                }
                regularUserMenu(user);
                break;
        }
    }

    private static void searchBooks(User user) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Id");
        int searchChoice = scanner.nextInt();
        scanner.nextLine();
        switch (searchChoice) {
            case 1:
                System.out.println("Enter Book Title to search:");
                String searchTitle = scanner.nextLine();
                Book foundBookByTitle = user.searchBooks(searchTitle);
                if (foundBookByTitle != null) {
                    System.out.println("Found Book: " + foundBookByTitle);
                } else {
                    System.out.println("No book found with title: " + searchTitle);
                }
                break;
            case 2:
                System.out.println("Enter Book ID to search:");
                int searchId = scanner.nextInt();
                scanner.nextLine();
                Book foundBookById = user.searchBooks(searchId);
                if (foundBookById != null) {
                    System.out.println("Found Book: " + foundBookById);
                } else {
                    System.out.println("No book found with ID: " + searchId);
                }
                break;
            default:
                System.out.println("Invalid choice, please try again.");
        }
    }
}