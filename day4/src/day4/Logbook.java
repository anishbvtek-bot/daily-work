package day4;

import java.util.*;
import java.util.logging.*;

public class Logbook {

    private static final Logger LOG = Logger.getLogger(Logbook.class.getName());

    static {
        LOG.setUseParentHandlers(true);
        LOG.setLevel(Level.INFO);
        Logger root = Logger.getLogger("");
        for (Handler h : root.getHandlers()) {
            h.setLevel(Level.ALL);
        }
    }

    private static class DuplicateBookException extends RuntimeException {
        DuplicateBookException(String message) { super(message); }
    }
    private static class InvalidPriceException extends RuntimeException {
        InvalidPriceException(String message) { super(message); }
    }
    private static class BookNotFoundException extends RuntimeException {
        BookNotFoundException(String message) { super(message); }
    }
    private static class AlreadyReservedException extends RuntimeException {
        AlreadyReservedException(String message) { super(message); }
    }

    private static class InvalidOptionException extends Exception {
        InvalidOptionException(String message) { super(message); }
    }

    private static class Book {
        String id;
        String title;
        String author;
        int price;
        String reservedBy;

        Book(String id, String title, String author, int price) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.price = price;
            this.reservedBy = null;
        }
        boolean isReserved() {
            return reservedBy != null && !reservedBy.isBlank();
        }
        public String toString() {
            String status = isReserved() ? ("RESERVED by " + reservedBy) : "Available";
            return String.format("{id=%s, title='%s', author='%s', price=%d, status=%s}",
                    id, title, author, price, status);
        }
    }

    private static final Scanner SC = new Scanner(System.in);
    private static final List<Book> BOOKS = new ArrayList<>();

    public static void main(String[] args) {
        BOOKS.add(new Book("A1", "zero to one", "anish", 100));
        BOOKS.add(new Book("B2", "shallow", "ashish", 200));

        System.out.println("Welcome to the Book Console!");
        LOG.info("Application started. Seeded initial books.");

        while (true) {
            try {
                int choice = showMenuAndGetChoice();
                LOG.info(() -> "Menu choice selected: " + choice);

                switch (choice) {
                    case 1 -> {
                        LOG.info("Action START: Add Book");
                        addBook();
                        LOG.info("Action END: Add Book");
                    }
                    case 2 -> {
                        LOG.info("Action START: Remove Book");
                        removeBook();
                        LOG.info("Action END: Remove Book");
                    }
                    case 3 -> {
                        LOG.info("Action START: Reserve Book");
                        reserveBook();
                        LOG.info("Action END: Reserve Book");
                    }
                    case 4 -> {
                        LOG.info("Action START: Display Books");
                        displayBooks();
                        LOG.info("Action END: Display Books");
                    }
                    case 5 -> {
                        LOG.info("Exit selected. Shutting down.");
                        System.out.println("Exiting... Goodbye!");
                        return;
                    }
                    default -> {
                        LOG.warning("Reached unexpected default case in main menu.");
                        throw new InvalidOptionException("Invalid menu option: " + choice);
                    }
                }
            } catch (InvalidOptionException e) {
                LOG.log(Level.WARNING, "Invalid option used in menu flow", e);
                System.out.println("[Error] " + e.getMessage());
            } catch (InputMismatchException e) {
                LOG.log(Level.WARNING, "Input mismatch (expected number)", e);
                System.out.println("[Error] Please enter a valid number.");
                SC.nextLine(); 
            } catch (RuntimeException e) {
                LOG.log(Level.SEVERE, "Runtime exception occurred", e);
                System.out.println("[Runtime Error] " + e.getMessage());
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Unexpected error in main loop", e);
                System.out.println("[Unexpected Error] " + e.getMessage());
            }
        }
    }

    private static int showMenuAndGetChoice() throws InvalidOptionException {
        System.out.println("\nMENU :");
        System.out.println("1. Add Book");
        System.out.println("2. Remove Book");
        System.out.println("3. Reserve Book");
        System.out.println("4. Display Books");
        System.out.println("5. Exit");
        System.out.print("Choose an option (1-5): ");

        if (!SC.hasNextInt()) {
            SC.nextLine();
            throw new InvalidOptionException("Choice must be a number between 1 and 5.");
        }
        int choice = SC.nextInt();
        SC.nextLine();
        if (choice < 1 || choice > 5) {
            throw new InvalidOptionException("Choice must be between 1 and 5.");
        }
        return choice;
    }

    private static void addBook() {
        System.out.println("\n-- Add a Book --");

        System.out.print("Enter Book ID (e.g., C3): ");
        String id = nonEmptyLine();
        if (findById(id).isPresent()) {
            throw new DuplicateBookException("A book with ID '" + id + "' already exists.");
        }

        System.out.print("Enter Title: ");
        String title = nonEmptyLine();

        System.out.print("Enter Author: ");
        String author = nonEmptyLine();

        Integer price = askPrice();
        if (price == null || price <= 0) {
            throw new InvalidPriceException("Price must be a positive integer.");
        }

        BOOKS.add(new Book(id, title, author, price));
        LOG.info(() -> String.format("Book added: id=%s, title=%s, author=%s, price=%d", id, title, author, price));
        System.out.println("Book added successfully!");
    }

    private static void removeBook() throws InvalidOptionException {
        System.out.println("\n-- Remove a Book --");
        System.out.println("1. By Title");
        System.out.println("2. By ID");
        System.out.print("Choose an option (1-2): ");

        if (!SC.hasNextInt()) {
            SC.nextLine();
            throw new InvalidOptionException("Removal choice must be 1 or 2.");
        }
        int opt = SC.nextInt();
        SC.nextLine();

        switch (opt) {
            case 1 -> {
                System.out.print("Enter Title to remove: ");
                String title = nonEmptyLine();
                boolean removedAny = removeIf(b -> b.title.equalsIgnoreCase(title));
                if (removedAny) {
                    LOG.info(() -> "Removed book(s) by title: " + title);
                    System.out.println("Removed book(s) with title: " + title);
                } else {
                    throw new BookNotFoundException("No book found with title: " + title);
                }
            }
            case 2 -> {
                System.out.print("Enter ID to remove: ");
                String id = nonEmptyLine();
                boolean removed = removeIf(b -> b.id.equalsIgnoreCase(id));
                if (removed) {
                    LOG.info(() -> "Removed book by id: " + id);
                    System.out.println("Removed book with id: " + id);
                } else {
                    throw new BookNotFoundException("No book found with id: " + id);
                }
            }
            default -> throw new InvalidOptionException("Invalid removal option: " + opt);
        }
    }

    private static void reserveBook() throws InvalidOptionException {
        System.out.println("\n-- Reserve a Book --");
        System.out.println("1. By Title");
        System.out.println("2. By ID");
        System.out.print("Choose an option (1-2): ");

        if (!SC.hasNextInt()) {
            SC.nextLine();
            throw new InvalidOptionException("Reservation choice must be 1 or 2.");
        }
        int opt = SC.nextInt();
        SC.nextLine();

        Optional<Book> match;
        switch (opt) {
            case 1 -> {
                System.out.print("Enter Title to reserve: ");
                String title = nonEmptyLine();
                match = findFirstByTitle(title);
            }
            case 2 -> {
                System.out.print("Enter ID to reserve: ");
                String id = nonEmptyLine();
                match = findById(id);
            }
            default -> throw new InvalidOptionException("Invalid reservation option: " + opt);
        }

        if (match.isEmpty()) {
            throw new BookNotFoundException("No matching book found to reserve.");
        }

        Book b = match.get();
        if (b.isReserved()) {
            throw new AlreadyReservedException("Book '" + b.id + "' is already reserved by: " + b.reservedBy);
        }

        System.out.print("Enter your name to reserve: ");
        String name = nonEmptyLine();
        b.reservedBy = name;
        LOG.info(() -> "Book reserved: id=" + b.id + ", by=" + name);
        System.out.println("Book reserved successfully!");
    }

    private static void displayBooks() {
        System.out.println("\n-- Current Books --");
        if (BOOKS.isEmpty()) {
            System.out.println("(no books)");
            return;
        }
        for (Book b : BOOKS) {
            System.out.println(b);
        }
    }

    private static boolean removeIf(java.util.function.Predicate<Book> predicate) {
        boolean removed = false;
        for (Iterator<Book> it = BOOKS.iterator(); it.hasNext();) {
            Book b = it.next();
            if (predicate.test(b)) {
                it.remove();
                removed = true;
            }
        }
        return removed;
    }

    private static Optional<Book> findById(String id) {
        return BOOKS.stream().filter(b -> b.id.equalsIgnoreCase(id)).findFirst();
    }

    private static Optional<Book> findFirstByTitle(String title) {
        return BOOKS.stream().filter(b -> b.title.equalsIgnoreCase(title)).findFirst();
    }

    private static Integer askPrice() {
        System.out.print("Enter Price (integer): ");
        if (!SC.hasNextInt()) {
            SC.nextLine();
            return null;
        }
        int p = SC.nextInt();
        SC.nextLine();
        return p > 0 ? p : null;
    }

    private static String nonEmptyLine() {
        String s = SC.nextLine().trim();
        while (s.isEmpty()) {
            System.out.print("Value cannot be empty. Please re-enter: ");
            s = SC.nextLine().trim();
        }
        return s;
    }
}


