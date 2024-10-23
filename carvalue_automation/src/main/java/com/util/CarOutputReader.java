package com.util;

import java.io.*;
import java.util.*;

public class CarOutputReader {

    public static Map<String, CarDetails> readCarValues(String fileName) {
        Map<String, CarDetails> carDetailsMap = new HashMap<>();
        try {
            ClassLoader classLoader = CarOutputReader.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new FileNotFoundException("File not found: " + fileName);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                String regNumber = values[0].trim();
                String year = values[3].trim(); // Assuming the year is the 4th column in output.txt
                carDetailsMap.put(regNumber, new CarDetails(regNumber, year));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return carDetailsMap;
    }
}
