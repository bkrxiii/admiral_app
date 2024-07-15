package uk.ac.cardiff.phoenix.ilm.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class LevelsAndWorkshopsNavTest {
    private WebDriver driver;
    @LocalServerPort
    private int port;
    private String url;

    @BeforeEach
    public void setup() {
        url = "http://localhost:" + port + "/";
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void workshopAndLevelNavigation() {
        driver.get(url);
        driver.manage().window().setSize(new Dimension(945, 1032));
        driver.findElement(By.linkText("log in")).click();
        driver.findElement(By.id("username")).sendKeys("admin@example.com");
        driver.findElement(By.id("password")).sendKeys("passwordthatchromewillsave");
        driver.findElement(By.cssSelector(".btn")).click();
        driver.findElement(By.id("dropdownMenuButton")).click();
        driver.findElement(By.linkText("Level 3")).click();
        driver.findElement(By.id("dropdownMenuButton")).click();
        driver.findElement(By.linkText("Level 5")).click();
        driver.findElement(By.cssSelector(".dropdown:nth-child(5) > #dropdownMenuButton")).click();
        driver.findElement(By.linkText("Understanding Leadership")).click();
        driver.findElement(By.cssSelector(".dropdown:nth-child(5) > #dropdownMenuButton")).click();
        driver.findElement(By.linkText("Building Effective Teams")).click();
        driver.findElement(By.cssSelector(".dropdown:nth-child(5) > #dropdownMenuButton")).click();
        driver.findElement(By.linkText("Understanding Stress Management")).click();
        driver.findElement(By.cssSelector(".dropdown:nth-child(5) > #dropdownMenuButton")).click();
        driver.findElement(By.linkText("Planning Change in the Workplace")).click();
        driver.findElement(By.cssSelector(".dropdown:nth-child(5) > #dropdownMenuButton")).click();
        driver.findElement(By.linkText("Solving Problems & Making Decisions")).click();
        driver.findElement(By.cssSelector(".dropdown:nth-child(5) > #dropdownMenuButton")).click();
        driver.findElement(By.linkText("Managing Workplace Projects")).click();
        driver.findElement(By.cssSelector(".dropdown:nth-child(5) > #dropdownMenuButton")).click();
        driver.findElement(By.linkText("Becoming an Effective Leader")).click();
        driver.findElement(By.cssSelector(".dropdown:nth-child(5) > #dropdownMenuButton")).click();
        driver.findElement(By.linkText("Leading Innovation & Change")).click();
        driver.findElement(By.cssSelector(".dropdown:nth-child(5) > #dropdownMenuButton")).click();
        driver.findElement(By.linkText("Leading Innovation & Change")).click();
        driver.findElement(By.cssSelector(".dropdown:nth-child(5) > #dropdownMenuButton")).click();
        driver.findElement(By.linkText("Understanding Organisational Culture")).click();
        driver.findElement(By.cssSelector(".dropdown:nth-child(5) > #dropdownMenuButton")).click();
        driver.findElement(By.linkText("Managing Stress & Conflict")).click();
        System.out.println("Workshop and Level Navigation Test Passed");
    }

    @Test
    public void viewLevel3() {
        driver.get(url);
        driver.manage().window().setSize(new Dimension(945, 1032));
        driver.findElement(By.linkText("log in")).click();
        driver.findElement(By.id("username")).sendKeys("admin@example.com");
        driver.findElement(By.id("password")).sendKeys("passwordthatchromewillsave");
        driver.findElement(By.cssSelector(".btn")).click();
        driver.findElement(By.id("dropdownMenuButton")).click();
        driver.findElement(By.linkText("Level 3")).click();
        assertEquals(driver.findElement(By.cssSelector("h2")).getText(), ("Level 3"));
        assertEquals(driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(1)")).getText(), ("Understanding Leadership"));
        assertEquals(driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(1)")).getText(), ("Building Effective Teams"));
        assertEquals(driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(1)")).getText(), ("Understanding Stress Management"));
        assertEquals(driver.findElement(By.cssSelector("tr:nth-child(4) > td:nth-child(1)")).getText(), ("Planning Change in the Workplace"));
        assertEquals(driver.findElement(By.cssSelector("tr:nth-child(5) > td:nth-child(1)")).getText(), ("Solving Problems & Making Decisions"));
        assertEquals(driver.findElement(By.cssSelector("tr:nth-child(6) > td:nth-child(1)")).getText(), ("Managing Workplace Projects"));
    }

    @Test
    public void viewLevel5() {
        driver.get(url);
        driver.manage().window().setSize(new Dimension(945, 1032));
        driver.findElement(By.linkText("log in")).click();
        driver.findElement(By.id("username")).sendKeys("admin@example.com");
        driver.findElement(By.id("password")).sendKeys("passwordthatchromewillsave");
        driver.findElement(By.cssSelector(".btn")).click();
        driver.findElement(By.id("dropdownMenuButton")).click();
        driver.findElement(By.linkText("Level 5")).click();
        assertEquals(driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(1)")).getText(), ("Becoming an Effective Leader"));
        assertEquals(driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(1)")).getText(), ("Leading Innovation & Change"));
        assertEquals(driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(1)")).getText(), ("Understanding Organisational Culture"));
        assertEquals(driver.findElement(By.cssSelector("tr:nth-child(4) > td:nth-child(1)")).getText(), ("Managing Stress & Conflict"));
    }
}
