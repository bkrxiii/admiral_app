package uk.ac.cardiff.phoenix.ilm.programs.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Data
@Entity
@NoArgsConstructor
@Audited
public class Workshop {
    // Workshop class is used to create the workshop table in the database
    // workshopId is primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long workshopId;
    // workshopName is a string version of workshop name. e.g. "Workshop 1"
    private String workshopName;
    // workshopDescription is there in case Niel wants to add any extra descriptions of the workshops (can be deleted if not needed)
    private String workshopDescription;
    // compulsory is a boolean to indicate whether the workshop is compulsory or not
    private Boolean compulsory;
    // componentOf is the level object that workshop is a part of
    // Many to One relationship in database
    // Corresponding column in level table is "workshops" list
    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level componentOf;

    // Constructor for workshop class with all fields
    public Workshop(String workshopName, String workshopDescription, Boolean compulsory, Level componentOf) {
        this.workshopName = workshopName;
        this.workshopDescription = workshopDescription;
        this.compulsory = compulsory;
        this.componentOf = componentOf;
    }

    public String toString() {
        return workshopName;
    }

    public Level getLevel() {
        return componentOf;
    }
}


