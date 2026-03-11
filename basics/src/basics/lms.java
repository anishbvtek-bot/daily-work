package basics;
import java.util.*;

class Book {
    String title;
    boolean available;
    STATUS status;
    int price;

    Book(String title,int price) {
        this.title = title;
        this.available = true;
        if (price > 0) {
        	this.price = price;
        }
        else {
        	throw new IllegalArgumentException("not possible");
        }
        
    }
}

class Library {

    List<Book> books = new ArrayList<>();

    void addBook(String title) {
        books.add(new Book(title,0));
    }

    void borrowBook(String title) {
        for(Book b : books) {
            if(b.title.equals(title) && b.available) {
                b.available = false;
                System.out.println("Borrowed: " + title);
                return;
            }
        }
        System.out.println("Book not available");
    }
    

    void displayBooks() {
        for(Book b : books)
            if(b.available)
                System.out.println(b.title);
    }
}
