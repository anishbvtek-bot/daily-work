package basics;

import java.util.List;
import java.util.ArrayList;

public class lists {
	public static void main(String[] args) {
		List<String> books = new ArrayList<>();
		books.add("book1");
		books.add("book2");
		System.out.println(books.size());
		books.forEach((book)-> System.out.println(book));
	}

}
