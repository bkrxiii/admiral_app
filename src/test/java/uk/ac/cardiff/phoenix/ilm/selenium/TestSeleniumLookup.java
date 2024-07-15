package uk.ac.cardiff.phoenix.ilm.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class TestSeleniumLookup {
    private WebDriver driver;
    //this gets the port - - we are randomising it below so it doesn't interfere with the actual app
    @LocalServerPort
    private int port;

    private String url;

    //Use useful project that makes creating a driver easier
    @BeforeEach
    public void setup() {
        url = "http://localhost:" + port + "/";
        WebDriverManager.chromedriver().setup();


        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        driver = new ChromeDriver(options);
    }

    //kill the driver after each test
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DirtiesContext
    public void testLookUpSelenium() {
        driver.get(url);
        driver.manage().window().setSize(new Dimension(1322, 1392));
        driver.findElement(By.linkText("log in")).click();
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).sendKeys("su@su.com");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.cssSelector(".btn")).click();
        driver.findElement(By.linkText("Candidate")).click();
        driver.findElement(By.linkText("Lookup")).click();
        driver.findElement(By.linkText("IT")).click();
        // check only IT candidates are displayed
        List<WebElement> departmentElementsIT = driver.findElements(By.cssSelector("table#candidates tbody tr td:nth-child(6) a"));
        List<String> departmentsITCheck = new ArrayList<>();
        for (WebElement departmentElementIT : departmentElementsIT) {
            departmentsITCheck.add(departmentElementIT.getText());
        }
        for (String i : departmentsITCheck) {
            assertEquals("IT", i);
        }
        driver.findElement(By.linkText("Lookup")).click();
        driver.findElement(By.linkText("Claims")).click();
        // check only Claims candidates are displayed
        List<WebElement> departmentElementsClaims = driver.findElements(By.cssSelector("table#candidates tbody tr td:nth-child(6) a"));
        List<String> departmentsClaimsCheck = new ArrayList<>();
        for (WebElement departmentElementClaims : departmentElementsClaims) {
            departmentsClaimsCheck.add(departmentElementClaims.getText());
        }
        for (String i : departmentsClaimsCheck) {
            assertEquals("Claims", i);
        }
        driver.findElement(By.linkText("Lookup")).click();
        driver.findElement(By.linkText("Operations")).click();
        // check only Operations candidates are displayed
        List<WebElement> departmentElementsOperations = driver.findElements(By.cssSelector("table#candidates tbody tr td:nth-child(6) a"));
        List<String> departmentsOperationsCheck = new ArrayList<>();
        for (WebElement departmentElementOperations : departmentElementsOperations) {
            departmentsOperationsCheck.add(departmentElementOperations.getText());
        }
        for (String i : departmentsOperationsCheck) {
            assertEquals("Operations", i);
        }
        driver.findElement(By.linkText("Lookup")).click();
        driver.findElement(By.linkText("Finance")).click();
        // check only Finance candidates are displayed
        List<WebElement> departmentElementsFinance = driver.findElements(By.cssSelector("table#candidates tbody tr td:nth-child(6) a"));
        List<String> departmentsFinanceCheck = new ArrayList<>();
        for (WebElement departmentElementFinance : departmentElementsFinance) {
            departmentsFinanceCheck.add(departmentElementFinance.getText());
        }
        for (String i : departmentsFinanceCheck) {
            assertEquals("Finance", i);
        }

        driver.findElement(By.linkText("Lookup")).click();
        driver.findElement(By.cssSelector("tr:nth-child(11) > td:nth-child(6) > a")).click();
        driver.findElement(By.linkText("Lookup")).click();

    }
}