// Ai was used as on influence

package uk.ac.cardiff.phoenix.ilm.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor // This is to make sure JPA can do its work here.
@Audited
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 50)
    private String firstName;
    @Column(length = 50)
    private String lastName;
    private LocalDate dateOfBirth; // I want a date picker to select this later -- do I need to format this later with
    private long payrollNumber;
    @Column(length = 50)
    private String department;
    @Column(length = 50)
    private String role;
    @Nullable
    private LocalDate registrationDate;
    @Column(unique = true)
    @Nullable
    private Long registrationNumber; // ILM number
    @Column(unique = true, length = 50)
    private String email;
    @ManyToOne
    @Nullable
    private Level level;

    // without level
    public Candidate(String firstName, String lastName, LocalDate dateOfBirth, long payrollNumber, String department, String role, LocalDate registrationDate, Long registrationNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.payrollNumber = payrollNumber;
        this.department = department;
        this.role = role;
        this.registrationDate = registrationDate;
        this.registrationNumber = registrationNumber;
        this.email = email;
    }

    // with level
    public Candidate(String firstName, String lastName, LocalDate dateOfBirth, long payrollNumber, String department, String role, LocalDate registrationDate, Long registrationNumber, String email, Level level) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.payrollNumber = payrollNumber;
        this.department = department;
        this.role = role;
        this.registrationDate = registrationDate;
        this.registrationNumber = registrationNumber;
        this.email = email;
        this.level = level;
    }

    public Candidate(String firstName, String lastName, LocalDate dateOfBirth, long payrollNumber, String department, String role, LocalDate registrationDate,  String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.payrollNumber = payrollNumber;
        this.department = department;
        this.role = role;
        this.registrationDate = registrationDate;
        this.email = email;
    }


    // adding toString() method to Candidate.java to print in console to make dev easier for me, can remove later
    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", payrollNumber=" + payrollNumber +
                ", department='" + department + '\'' +
                ", role='" + role + '\'' +
                ", registrationDate=" + registrationDate +
                ", registrationNumber=" + registrationNumber +
                ", email='" + email + '\'' +
                ", level=" + level +
                '}';
    }
}
