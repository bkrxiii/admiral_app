package uk.ac.cardiff.phoenix.ilm.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;


public class PasswordUtil implements PasswordEncoder {
    private static final int SALTSIZE = 16;

    private static final int KEY_LENGTH = 512;
    private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);
    private static final int ITERATIONS;

    static {
        int iterationsTemp;
        try {
            // Fetching the environment variable directly as Spring's Environment isn't available in static context
            String[] activeProfiles = System.getProperty("spring.profiles.active", "").split(",");
            if (Arrays.asList(activeProfiles).contains("devintellij")) {
                iterationsTemp = 1000; // Reduced complexity for 'devintellij' profile
            } else {
                iterationsTemp = 65536 * 22; // Default complexity
            }
        } catch (Exception e) {
            iterationsTemp = 65536 * 22; // Default in case of any failure
        }
        ITERATIONS = iterationsTemp;
    }




    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALTSIZE];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    public static String hashPassword(String password, String salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATIONS, KEY_LENGTH);

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            long startTime = System.currentTimeMillis();
            byte[] hash = factory.generateSecret(spec).getEncoded();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            if (duration < 800) {
                logger.warn("Hashing took " + duration + "ms" + "complexity is too low");
            } else {
                logger.debug("Hashing took " + duration + "ms");
            }

            return salt + bytesToHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {throw new RuntimeException(e);}


    }

    private static String bytesToHex(byte[] salt) {
        StringBuilder builder = new StringBuilder();
        for (byte b: salt) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }


    @Override
    public String encode(CharSequence rawPassword) {
        String salt = generateSalt();

        return hashPassword(rawPassword.toString(), salt);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // Extract the salt from the encoded password
        String salt = encodedPassword.substring(0, PasswordUtil.SALTSIZE * 2);

        // Hash the raw password with the extracted salt
        String hashedPassword = hashPassword(rawPassword.toString(), salt);

        // Compare the hashed password with the encoded password
        return hashedPassword.equals(encodedPassword);
    }


}

