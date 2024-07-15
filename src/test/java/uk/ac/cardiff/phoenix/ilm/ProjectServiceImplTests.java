// Ai used as an inspiration

package uk.ac.cardiff.phoenix.ilm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import uk.ac.cardiff.phoenix.ilm.programs.model.Workshop;
import uk.ac.cardiff.phoenix.ilm.programs.service.LevelsService;
import uk.ac.cardiff.phoenix.ilm.programs.service.WorkshopsService;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureTestDatabase
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProjectServiceImplTests {

    @Autowired
    private LevelsService levelsService;
    @Autowired
    private WorkshopsService workshopsService;

    @Test
    @DirtiesContext
    @AutoConfigureTestDatabase
    void existingLevelWontAdd() {
        levelsService.addLevel("Level 1", "Level 1", 1);
        assertThrows(DataIntegrityViolationException.class, () -> levelsService.addLevel("Level 1", "Level 1", 1));
    }

    // Faviobecker: pair-programmed with Archer
    @Test
    @DirtiesContext
    @AutoConfigureTestDatabase
    void addWorkshopToExistingLevel(){
        //create a level
        levelsService.addLevel("Level 6", "Level 6", 6);
        // create a workshop and connect it to the level
        workshopsService.addWorkshop("Workshop 1", "Workshop 1", true, levelsService.getLevelByInteger(6));
        // print out name of workshop
        String level = workshopsService.getWorkshopById(1L).getLevel().getLevelName();
        // assert that the size of the list of workshops in the level is 1
        assertEquals("Level 6", level);
    }

    @AutoConfigureTestDatabase
    @Test
    @DirtiesContext
    void getLevelByInteger(){
        //create a level
        levelsService.addLevel("Level 6", "Level 6", 6);
        // assert that the level returned by getLevelByInteger is the same as the level created
        assertEquals(levelsService.getLevelByInteger(6).getLevelName(), "Level 6");
    }

    @AutoConfigureTestDatabase
    @Test
    @DirtiesContext
    void getLevelById(){
        //create a level
        levelsService.addLevel("Level 6", "Level 6", 6);
        // assert that the level returned by getLevelById is the same as the level created
        assertEquals(levelsService.getLevelById(1L).getLevelName(), "Level 6");
    }

    @AutoConfigureTestDatabase
    @Test
    @DirtiesContext
    void getLevelToString(){
        //create a level
        levelsService.addLevel("Level 6", "Level 6", 6);
        // assert that the level returned by getLevelById is the same as the level created
        assertEquals(levelsService.getLevelById(1L).toString(), "Level 6");
    }

    @AutoConfigureTestDatabase
    @Test
    @DirtiesContext
    void getWorkshopToString(){
        //create a level
        levelsService.addLevel("Level 6", "Level 6", 6);
        // create a workshop and connect it to the level
        workshopsService.addWorkshop("Workshop 1", "Workshop 1", true, levelsService.getLevelByInteger(6));
        // assert that the level returned by getLevelById is the same as the level created
        assertEquals(workshopsService.getWorkshopById(1L).toString(), "Workshop 1");
    }

    @AutoConfigureTestDatabase
    @Test
    @DirtiesContext
    void getAllLevels(){
        levelsService.addLevel("Level 1", "Level 1", 1);
        levelsService.addLevel("Level 2", "Level 2", 2);
        levelsService.addLevel("Level 3", "Level 3", 3);
        levelsService.addLevel("Level 4", "Level 4", 4);
        levelsService.addLevel("Level 5", "Level 5", 5);
        levelsService.addLevel("Level 6", "Level 6", 6);
        List<Level> levels = levelsService.getLevels();
        assertEquals(levels.size(), 6);
        for (int i = 0; i < levels.size(); i++) {
            assertEquals(levels.get(i).getLevelNumber(), i+1);
        }
    }

    @AutoConfigureTestDatabase
    @Test
    @DirtiesContext
    void getAllWorkshops(){
        levelsService.addLevel("Level 1", "Level 1", 1);
        levelsService.addLevel("Level 2", "Level 2", 2);
        workshopsService.addWorkshop("Workshop 1", "Workshop 1", true, levelsService.getLevelByInteger(1));
        workshopsService.addWorkshop("Workshop 2", "Workshop 2", true, levelsService.getLevelByInteger(1));
        workshopsService.addWorkshop("Workshop 3", "Workshop 3", true, levelsService.getLevelByInteger(1));
        workshopsService.addWorkshop("Workshop 4", "Workshop 4", true, levelsService.getLevelByInteger(2));
        workshopsService.addWorkshop("Workshop 5", "Workshop 5", true, levelsService.getLevelByInteger(2));
        workshopsService.addWorkshop("Workshop 6", "Workshop 6", true, levelsService.getLevelByInteger(2));
        List<Workshop> workshops = workshopsService.getWorkshops();
        assertEquals(workshops.size(), 6);
        for (int i = 0; i < workshops.size(); i++) {
            assertEquals(workshops.get(i).getWorkshopName(), "Workshop " + (i+1));
        }
    }

    @AutoConfigureTestDatabase
    @Test
    @DirtiesContext
    void getAllWorkshopsByLevel(){
        levelsService.addLevel("Level 1", "Level 1", 1);
        levelsService.addLevel("Level 2", "Level 2", 2);
        workshopsService.addWorkshop("Workshop 1", "Workshop 1", true, levelsService.getLevelByInteger(1));
        workshopsService.addWorkshop("Workshop 2", "Workshop 2", true, levelsService.getLevelByInteger(1));
        workshopsService.addWorkshop("Workshop 3", "Workshop 3", true, levelsService.getLevelByInteger(1));
        workshopsService.addWorkshop("Workshop 4", "Workshop 4", true, levelsService.getLevelByInteger(2));
        workshopsService.addWorkshop("Workshop 5", "Workshop 5", true, levelsService.getLevelByInteger(2));
        workshopsService.addWorkshop("Workshop 6", "Workshop 6", true, levelsService.getLevelByInteger(2));
        Set<Workshop> workshops = workshopsService.getWorkshopsByLevelNumber(2);
        assertEquals(workshops.size(), 3);
        for (int i = 0; i < workshops.size(); i++) {
            assertEquals(workshops.toArray()[i].toString(), "Workshop " + (i+4));
        }
    }



}
