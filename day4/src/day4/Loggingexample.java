package day4;

import java.util.logging.*;

public class Loggingexample { 
    
    
    private static final Logger logger = Logger.getLogger(Loggingexample.class.getName());

    public static void main(String[] args) {
        logger.info("Application started");
        logger.warning("Low memory");
        logger.severe("System failure");
    }
}