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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class TestSeleniumAddCandidate {
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
    public void testAddCandidateSelenium() {
        driver.get(url);
        driver.manage().window().setSize(new Dimension(1322, 1392));
        driver.findElement(By.linkText("log in")).click();
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).sendKeys("su@su.com");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.cssSelector(".btn")).click();
        driver.findElement(By.linkText("Candidate")).click();
        driver.findElement(By.linkText("Lookup")).click();

        // amount of candidates before adding new candidate by id
        List<WebElement> totalIds = driver.findElements(By.cssSelector("tbody tr td:first-child a[href*='/lookup/']"));
        int totalIdsSize = totalIds.size();

        driver.findElement(By.linkText("Submission")).click();
        driver.findElement(By.id("firstName")).sendKeys("Favio");
        driver.findElement(By.id("lastName")).sendKeys("Becker");
        driver.findElement(By.id("dateOfBirth")).sendKeys("13-11-1980");
        driver.findElement(By.id("payrollNumber")).sendKeys("80250");
        driver.findElement(By.id("department")).sendKeys("Nth");
        driver.findElement(By.id("role")).sendKeys("nth");
        driver.findElement(By.id("registrationDate")).sendKeys("11-11-2023");
        driver.findElement(By.id("registrationNumber")).sendKeys("80250");
        driver.findElement(By.id("email")).sendKeys("favio@admiral.com");
        driver.findElement(By.cssSelector("button:nth-child(7)")).click();


        driver.findElement(By.linkText("Lookup")).click();

        List<WebElement> newTotalIds = driver.findElements(By.cssSelector("tbody tr td:first-child a[href*='/lookup/']"));
        int newTotalIdsSize = newTotalIds.size();
        int checkAgainst = totalIdsSize + 1;

        assertEquals(checkAgainst, newTotalIdsSize);
    }
}