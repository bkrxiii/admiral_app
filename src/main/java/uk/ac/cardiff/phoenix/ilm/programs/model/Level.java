package uk.ac.cardiff.phoenix.ilm.programs.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.util.List;


@Data
@Entity
@NoArgsConstructor
@Audited
public class Level {

    // Level class is used to create the level table in the database
    // levelId is primary key
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long levelId;
    // levelName is a string version of level name. e.g. "Level 3"
    private String levelName;
    // levelDescription is there in case Niel wants to add any extra descriptions of the levels (can be deleted if not needed)
    private String levelDescription;
    // levelNumber is an integer version of level name (in case needed for sorting)
    @Column(unique = true)
    private int levelNumber;
    // workshops is a list of workshops that are part of the level
    // One to Many relationship in database
    // Corresponding column in workshop table is "componentOf"
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "componentOf")
    private List<Workshop> workshops;


    // Constructor for level class with all fields
    public Level(long levelId, String levelName, String levelDescription, int levelNumber, List<Workshop> workshops) {
        this.levelId = levelId;
        this.levelName = levelName;
        this.levelDescription = levelDescription;
        this.levelNumber = levelNumber;
        this.workshops = workshops;
    }

    // Constructor for level class with only name, description and number
    public Level(String levelName, String levelDescription, int levelNumber){
        this.levelName = levelName;
        this.levelDescription = levelDescription;
        this.levelNumber = levelNumber;
    }

    public String toString() {
        return levelName;
    }
}
