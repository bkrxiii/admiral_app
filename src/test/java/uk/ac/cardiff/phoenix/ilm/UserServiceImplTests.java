package uk.ac.cardiff.phoenix.ilm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import uk.ac.cardiff.phoenix.ilm.users.model.User;
import uk.ac.cardiff.phoenix.ilm.users.service.UserService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureTestDatabase
@SpringBootTest
class UserServiceImplTests {
    @Autowired
    private UserService userService;

    @Test
    void saltedPasswordAuthenticates() {
        User user = new User("te", "new", "asdadc@gmail.com");
        userService.addUser(user);
        userService.changePassword(user, "password");
        assertTrue(userService.authenticate("asdadc@gmail.com", "password"));
    }

    @Test
    void incorrectPasswordDoesNotAuthenticate() {
        User user = new User("te", "new", "cscsc@gmail.com");
        userService.addUser(user);
        userService.changePassword(user, "password");
        assertFalse(userService.authenticate("cscsc@gmail.com", "X"));
    }

    @Test
    void incorrectEmailDoesNotAuthenticate() {
        User user = new User("te", "new", "email1@gmail.com");
        userService.addUser(user);
        userService.changePassword(user, "password");
        assertFalse(userService.authenticate("wrongemail@gmail.com", "password"));
    }


}

