package com.rubenlaguna;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger("chapters.introduction.HelloWorld1");
        String name = generateRandomString(6);
        logger.debug("Hello world, {}", name);

    }
    
    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        
        return sb.toString();
    }
}
