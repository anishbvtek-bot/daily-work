package basics;
import java.util.*;
public class collectionexample {
	public static void main(String[] args) {
		List<String> cities = new ArrayList<>();
		//System.out.println(cities.remove("mysore"));
		System.out.println(cities.set(2,"mysore"));
		System.out.println(cities);
	}

	private static List<String> createListofCities(){
		List<String> cities = new ArrayList<>();
		cities.add("mysore");
		cities.add("bangalore");
		cities.add("manglore");
		return cities;
	}

		
		
		
		
	}


