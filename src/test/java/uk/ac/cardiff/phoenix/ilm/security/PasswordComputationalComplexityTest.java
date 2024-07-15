package uk.ac.cardiff.phoenix.ilm.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestComponent
public class PasswordComputationalComplexityTest {



    @Test
    public void testHashingComplexity() {
        // Call hashiing fucntion prior to timing test to ensure that the JVM has loaded the required classes
        PasswordUtil.hashPassword("password", PasswordUtil.generateSalt());
        //Generate random 20 char password string
        String password = "foicme£4958£$29348544@3#4348";
        int numberOfIterations = 10;
        long totalTime = 0;
        for (int i = 0; i < numberOfIterations; i++) {
            long startTime = System.currentTimeMillis();
            PasswordUtil.hashPassword(password, PasswordUtil.generateSalt());
            long endTime = System.currentTimeMillis();
            totalTime += endTime - startTime;
        }

        long averageDuration = totalTime / numberOfIterations;

        long lowerBound = 500;
        long upperBound = 3000;

        assertTrue(lowerBound < averageDuration,"Average duration of hashing," + averageDuration + "ms, is less than " + lowerBound + "ms");
        assertTrue(averageDuration < upperBound,"Average duration of hashing," + averageDuration + "ms, is greater than " + upperBound + "ms");
        System.out.println("Average duration of hashing," + averageDuration + "ms, is between " + lowerBound + "ms and " + upperBound + "ms");
    }

}
