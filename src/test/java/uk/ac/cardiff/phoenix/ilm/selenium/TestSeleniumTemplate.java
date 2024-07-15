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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class TestSeleniumTemplate {
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
    public void downloadReport() {
        driver.get(url);
        driver.manage().window().setSize(new Dimension(1530, 807));
        // click on the login link and log in
        driver.findElement(By.linkText("log in")).click();
        driver.findElement(By.id("username")).sendKeys("admin@example.com");
        driver.findElement(By.id("password")).sendKeys("passwordthatchromewillsave");
        driver.findElement(By.cssSelector(".btn")).click();

        // these lines tell selenium to wait for the page to load before continuing, max 10 seconds
        // its actually waiting for the element with the class "navbar-heading" to be present
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement alertElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("navbar-heading")));

        String expectedMessage = "ILM Manager";
        String actualMessage = alertElement.getText();
        //Check that the element has the correct text
        assertEquals(expectedMessage, actualMessage, "Heading Missing");
    }
}
