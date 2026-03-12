package basics;

import java.util.ArrayList;
import java.util.List;

public class book {
    public static void main(String[] args) {
        List<String> books = ListOfTitles();
        System.out.println("Initial books: " + books + " | Book prices: " + ListOfPrices() + " | Book id: " + ListOfBookId() );  
        System.out.println("Removing zero to one: " + books.remove("zero to one"));
        System.out.println("updated books: " + books);
    }
    private static List<String> ListOfBookId() {
        List<String> bookId = new ArrayList<>();
        bookId.add("A1");
        bookId.add("B2");
        return bookId;
    }
    private static List<String> ListOfTitles() {
        List<String> title = new ArrayList<>();
        title.add("zero to one");
        title.add("shallow");
        return title;
    }
    private static List<String> ListOfPrices() {
        List<String> Price = new ArrayList<>();
        Price.add("100");
        Price.add("200");
        return Price;
    }
    private static List<String> ListOfAuthors() {
        List<String> author = new ArrayList<>();
        author.add("anish");
        author.add("ashish");
        return author;
    }
    }
   