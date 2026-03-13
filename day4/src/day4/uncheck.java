package day4;

import java.io.*;

public class uncheck {
	public static void main(String[] args) {
		try {
		String str = null;
		System.out.println(str.length());
		validateAge(17);
	}
	  catch(IllegalArgumentException e)
	{
		e.printStackTrace();
	}
	System.out.println("finished");

	}


	private static void validateAge(int age) {
		if (age < 18) {
			throw new IllegalArgumentException("age must be 18");
		}
	}
}
