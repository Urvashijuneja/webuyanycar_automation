package com.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class CarInputReader {

    /**
     * Reads car registration numbers from the input file and stores them in a Set to ensure uniqueness.
     *
     * @param fileName The name of the file containing the car registration numbers.
     * @return A set of unique car registration numbers extracted from the file.
     */
    public static Set<String> readCarRegistrations(String fileName) {
        Set<String> carRegistrationsSet = new HashSet<>();  // Set to ensure uniqueness
        try {
            // Load the file from the resources folder
            ClassLoader classLoader = CarInputReader.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(fileName);

            // Ensure the file was found
            if (inputStream == null) {
                throw new FileNotFoundException("File not found: " + fileName);
            }

            // Read the file and process the lines
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            // Regular expression to match valid UK car registrations
            Pattern pattern = Pattern.compile("[A-Z]{2}[0-9]{2}[A-Z]{3}|[A-Z]{2}[0-9]{2}\\s?[A-Z]{3}");

            while ((line = reader.readLine()) != null) {
                // Match and concatenate car registration parts
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String registration = matcher.group().replaceAll("\\s+", "");  // Remove any spaces between registration parts
                    carRegistrationsSet.add(registration);  // Add to Set to ensure uniqueness
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  // Handle the exception
        }

        return carRegistrationsSet;  // Return the Set of car registrations
    }
}
