package com.webuyanycar;

import com.util.CarDetails;
import com.util.CarInputReader;
import com.microsoft.playwright.*;
import com.util.CarOutputReader;
import io.cucumber.java.en.*;

import java.util.*;

import static org.junit.Assert.*;

import com.microsoft.playwright.options.AriaRole;


public class CarValueSteps {

    private Browser browser;
    private Page page;
    Set<String> carRegistrations = new HashSet<>();
    private Map<String, CarDetails> expectedCarValues;
    private String carValueFromWebsite;

    // 1. Step to load car registrations from the input file

    @Given("the user has noted the car registration numbers from {string}")
    public void loadCarRegistrations(String inputFile) {
        carRegistrations = CarInputReader.readCarRegistrations(inputFile); // Read from the input file
        assertNotNull("Car registrations should not be null", carRegistrations);
        //Need to replace this with logger statement in real scenario
        System.out.println("TEST ! STEP" + carRegistrations.toString());
    }

    // 2. Step to enter each registration number on the website
    @When("the user enters each registration number from the input file on the webuyanycar website")
    public void enterRegistrationNumbersOnWebsite() {
        //This will move in world.java file in real scenario and hooks has to define for cucumber
        browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        BrowserContext context = browser.newContext();
        page = context.newPage();
        page.navigate("https://www.webuyanycar.com/car-valuation");
        page.locator("role=button[name='Accept all cookies']").click();

        for (String regNumber : carRegistrations) {
            page.fill("input[placeholder='Enter your reg']", regNumber);
            System.out.println(regNumber);

            // This will go inpage objects folder and page classes in real scenario
            // Adjust the selector based on the website's HTML structure
            page.fill("input.question-input.icon-text", " ");
            page.fill("input.question-input.icon-text", "32000");
            //page.fill("input[placeholder='Mileage']", "32000");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Get my car valuation")).click();
            page.waitForTimeout(3000);

            if (page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Sorry, we couldn't find your car")).isVisible()) {
                System.out.println("Car not found on website for registration: " + regNumber);
            } else {
                System.out.println("Car found on website for registration: " + regNumber);
                String regNo = page.textContent("(//div[@class='details-vrm ng-star-inserted'])[2]");
                String year = page.textContent("(//div[@class='d-table']//div[contains(text(), 'Year:')]/following-sibling::div)[2]");
                page.navigate("https://www.webuyanycar.com/car-valuation");

                compareCarDetailsWithOutput(regNumber, regNo, year);  // Call the comparison method
                page.navigate("https://www.webuyanycar.com/car-valuation"); // Reset for the next registration
            }
        }
    }

    // 4. Step to compare the car value from the website with the output file
    @Then("the user compares the car value mentioned in the {string}")
    public void compareWithOutputFile(String outputFile) {
        expectedCarValues = CarOutputReader.readCarValues(outputFile); // Read expected car details from output.txt
        assertNotNull("Expected car values should not be null", expectedCarValues);
    }

    // Helper method to compare the website data with the output file data
    private void compareCarDetailsWithOutput(String regNumber, String regNoFromWebsite, String yearFromWebsite) {

        CarDetails expectedDetails = expectedCarValues.get(regNumber); // Get the expected details from output file

        assertNotNull("Expected details should not be null for registration: " + regNumber, expectedDetails);
        assertEquals("Registration number does not match for " + regNumber, expectedDetails.getVariantReg(), regNoFromWebsite);
        assertEquals("Year does not match for " + regNumber, expectedDetails.getYear(), yearFromWebsite);
    }
}
