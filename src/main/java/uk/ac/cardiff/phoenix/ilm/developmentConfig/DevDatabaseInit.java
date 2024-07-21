package uk.ac.cardiff.phoenix.ilm.developmentConfig;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import uk.ac.cardiff.phoenix.ilm.programs.service.HomeworkService;
import uk.ac.cardiff.phoenix.ilm.programs.service.LevelsService;
import uk.ac.cardiff.phoenix.ilm.programs.service.WorkshopsService;
import uk.ac.cardiff.phoenix.ilm.service.CandidateService;
import uk.ac.cardiff.phoenix.ilm.users.model.RolePermission;
import uk.ac.cardiff.phoenix.ilm.users.model.User;
import uk.ac.cardiff.phoenix.ilm.users.model.UserRole;
import uk.ac.cardiff.phoenix.ilm.users.service.RolePermissionService;
import uk.ac.cardiff.phoenix.ilm.users.service.UserRoleService;
import uk.ac.cardiff.phoenix.ilm.users.service.UserService;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@Profile("dev")
public class DevDatabaseInit {

    private final UserService userService;
    private final UserRoleService userRoleService;
    private final RolePermissionService rolePermissionService;
    private final LevelsService levelService;
    private final WorkshopsService workshopService;
    private final CandidateService  candidateService;
    private final HomeworkService homeworkService;


    public DevDatabaseInit(UserService userService, UserRoleService userRoleService, RolePermissionService rolePermissionService, LevelsService levelsService, WorkshopsService workshopsService, CandidateService candidateService, HomeworkService homeworkService) {
        this.userRoleService = userRoleService;
        this.userService = userService;
        this.rolePermissionService = rolePermissionService;
        this.levelService = levelsService;
        this.workshopService = workshopsService;
        this.candidateService = candidateService;
        this.homeworkService = homeworkService;
    }


    @Bean
    public CommandLineRunner loadDevData() {
        return args -> {
            //Create user with TUTOR permission
            Set<RolePermission> TUTORPermissions = Stream.of(
                    "SCHEDULE_WORKSHOP", "CONFIRM_ATTENDANCE")
                    .map(rolePermissionService::addPermission)
                    .collect(Collectors.toSet());

            UserRole TUTORRole = userRoleService.addUserRole("TUTOR");
            userRoleService.addPermissionToRole(TUTORRole, TUTORPermissions);

            User UserTutor = new User("John", "Tutor", "john.tutor@example.com");
            userService.addUser(UserTutor);
            userService.changePassword(UserTutor, "password");
            userService.addRole(UserTutor, TUTORRole);
            System.out.println(UserTutor);

            //Create user with ADMIN privileges

            Set<RolePermission> ADMINPermissions = Stream.of(
                            "ADMIN","ADMIN_UPLOAD")
                    .map(rolePermissionService::addPermission)
                    .collect(Collectors.toSet());

            UserRole ADMINRole = userRoleService.addUserRole("ADMIN");
            userRoleService.addPermissionToRole(ADMINRole, ADMINPermissions);

            User UserADMIN = new User("Dan", "Admin", "admin@example.com");
            userService.addUser(UserADMIN);
            userService.changePassword(UserADMIN, "passwordthatchromewillsave");
            userService.addRole(UserADMIN, ADMINRole);
            System.out.println(UserADMIN);


            //create superuser role
            Set<RolePermission> SUPermission = Stream.of(
                            "SU")
                    .map(rolePermissionService::addPermission)
                    .collect(Collectors.toSet());

            UserRole SURole = userRoleService.addUserRole("SU");
            userRoleService.addPermissionToRole(SURole, SUPermission);

            User UserSU = new User("John", "SU", "su@su.com");
            userService.addUser(UserSU);
            userService.changePassword(UserSU, "password");
            userService.addRole(UserSU, SURole);
            System.out.println(UserSU);

            // Create Levels
            Level level3 = new Level("Level 3", "Level 3 Description", 3);
            Level level5 = new Level("Level 5", "Level 5 Description", 5);
            levelService.addLevel(level3);
            levelService.addLevel(level5);
            System.out.println("Added Levels");


            // Create Workshops
            Level level3Object = levelService.getLevelByInteger(3);
            Level level5Object = levelService.getLevelByInteger(5);
            workshopService.addWorkshop("Understanding Leadership", "Understanding Leadership description", true, level3Object);
            workshopService.addWorkshop("Building Effective Teams", "Building Effective Teams description", false, level3Object);
            workshopService.addWorkshop("Understanding Stress Management", "Understanding Stress Management description", false, level3Object);
            workshopService.addWorkshop("Planning Change in the Workplace", "Planning Change in the Workplace description", false, level3Object);
            workshopService.addWorkshop("Solving Problems & Making Decisions", "Solving Problems & Making Decisions description", false, level3Object);
            workshopService.addWorkshop("Managing Workplace Projects", "Managing Workplace Projects description", false, level3Object);
            workshopService.addWorkshop("Becoming an Effective Leader ", "Becoming an Effective Leader description", true, level5Object);
            workshopService.addWorkshop("Leading Innovation & Change", "Leading Innovation & Change description", false, level5Object);
            workshopService.addWorkshop("Understanding Organisational Culture", "Understanding Organisational Culture description", false, level5Object);
            workshopService.addWorkshop("Managing Stress & Conflict", "Managing Stress & Conflict", false, level5Object);
            System.out.println("Added Workshops");

            //create candidates for testing
            LocalDate dateOfBirth = LocalDate.of(1970, 1, 1);
            LocalDate registrationDate = LocalDate.of(2023, 12, 1);
            Candidate candidate = new Candidate("John", "Wick", dateOfBirth, 123456, "IT", "Security", registrationDate, 123456L, "johnwick@aol.com");
            Candidate candidate2 = new Candidate("One", "One0", dateOfBirth, 234567, "Claims", "QA Engineer", registrationDate, 234567L, "test1@aol.com");
            Candidate candidate3 = new Candidate("Two", "Two0", dateOfBirth, 345678, "IT", "QA", registrationDate,  "test2@aol.com");


            // generate more candidates for demonstration

            Candidate candidate04 = new Candidate("Daniel", "Davis", LocalDate.of(2022, 3, 12), 345777, "Operations", "Operations Manager", LocalDate.of(2023, 2, 28), 348678L, "danieldavis@aol.com");
            Candidate candidate05 = new Candidate("Emily", "Evans", LocalDate.of(2021, 5, 8), 765111, "IT", "Security Engineer", LocalDate.of(2023, 5, 15), 765900L, "emilyevans@aol.com");
            Candidate candidate06 = new Candidate("Frank", "Foster", LocalDate.of(2020, 10, 21), 123333, "Finance", "Financial Analyst", LocalDate.of(2023, 9, 22), 103456L, "frankfoster@aol.com");
            Candidate candidate07 = new Candidate("Grace", "Garrett", LocalDate.of(2019, 7, 4), 567222, "Marketing", "Digital Marketing Specialist", LocalDate.of(2023, 8, 1), 538890L, "gracegarrett@aol.com");
            Candidate candidate08 = new Candidate("Henry", "Harrison", LocalDate.of(2022, 3, 7), 987987, "Operations", "Supply Chain Manager", LocalDate.of(2023, 4, 18), 987104L, "henryharrison@aol.com");
            Candidate candidate09 = new Candidate("Isabella", "Ives", LocalDate.of(2021, 5, 9), 341222, "IT", "Systems Administrator", LocalDate.of(2023, 6, 7), 345108L, "isabellaisles@aol.com");
            Candidate candidate10 = new Candidate("James", "Johnson", LocalDate.of(2020, 10, 13), 765788, "Finance", "Tax Accountant", LocalDate.of(2023, 10, 8), 711132L, "jamesjohnson@aol.com");
            Candidate candidate11 = new Candidate("Katie", "Kelly", LocalDate.of(2019, 7, 16), 122111, "Marketing", "Brand Manager", LocalDate.of(2023, 12, 6), "katiekenny@aol.com");
            Candidate candidate12 = new Candidate("Laura", "Lewis", LocalDate.of(2022, 3, 21), 567888, "Operations", "Operations Specialist", LocalDate.of(2023, 12, 4), "lauralewis@aol.com");
            Candidate candidate13 = new Candidate("Michael", "Moore", LocalDate.of(2021, 5, 5), 987778, "IT", "Network Engineer", LocalDate.of(2023, 1, 2), 987004L, "michaelmoore@aol.com");
            Candidate candidate14 = new Candidate("Natalie", "Nelson", LocalDate.of(2020, 10, 11), 345123, "Finance", "Financial Analyst", LocalDate.of(2023, 2, 3), 300678L, "natalienelson@test.com");
            Candidate candidate15 = new Candidate("Peter", "Park", LocalDate.of(2019, 7, 8), 765181, "Marketing", "SEO Specialist", LocalDate.of(2023, 1, 18), 765402L, "peterpark@aol.com");
            Candidate candidate16 = new Candidate("Robert", "Russell", LocalDate.of(2022, 3, 15), 123756, "Operations", "Project Manager", LocalDate.of(2023, 2, 10), 120456L, "robertrussell@aol.com");
            Candidate candidate17 = new Candidate("Sarah", "Smith", LocalDate.of(2021, 5, 1), 567870, "IT", "Web Developer", LocalDate.of(2023, 3, 24), 567090L, "sarahsmith@aol.com");
            Candidate candidate18 = new Candidate("Thomas", "Thompson", LocalDate.of(2020, 10, 17), 999654, "Finance", "Cost Accountant", LocalDate.of(2023, 4, 22), 907154L, "thomasthompson@aol.com");
            Candidate candidate19 = new Candidate("Victoria", "Walker", LocalDate.of(2019, 7, 9), 345778, "Marketing", "Content Marketing Specialist", LocalDate.of(2023, 5, 11), 341078L, "victoriarwalker@aol.com");
            Candidate candidate20 = new Candidate("William", "Wilson", LocalDate.of(2022, 3, 4), 777832, "Operations", "Supply Chain Analyst", LocalDate.of(2023, 6, 25), 710132L, "williamwilson@aol.com");
            Candidate candidate21 = new Candidate("Xavier", "Xavier", LocalDate.of(2021, 5, 22), 122356, "IT", "Cloud Architect", LocalDate.of(2023, 8, 3), 100056L, "xavierxavier@aol.com");
            Candidate candidate22 = new Candidate("Yasmin", "Young", LocalDate.of(2020, 10, 18), 567871, "Finance", "Financial Controller", LocalDate.of(2023, 9, 28), 512890L, "yasminyoung@aol.com");
            Candidate candidate23 = new Candidate("Zachariah", "Zimmerman", LocalDate.of(2019, 7, 6), 980110, "Marketing", "Marketing Operations Manager", LocalDate.of(2023, 10, 5), 900004L, "zachariahzimmerman@aol.com");
            Candidate candidate24 = new Candidate("Amanda", "Anderson", LocalDate.of(2022, 3, 19), 345080, "Operations", "Operations Manager", LocalDate.of(2023, 12, 2), "amandaanderson@test.com");
            Candidate candidate25 = new Candidate("Brandon", "Brown", LocalDate.of(2021, 5, 3), 765344, "IT", "Security Engineer", LocalDate.of(2023, 12, 4), "brandonbrown@admiralgroup.co.uk");
            Candidate candidate26 = new Candidate("Catherine", "Clark", LocalDate.of(2020, 10, 14), 119456, "Finance", "Financial Analyst", LocalDate.of(2023, 1, 3), 100156L, "catherineclark@admiralgroup.co.uk");
            Candidate candidate27 = new Candidate("David", "Davis", LocalDate.of(2019, 7, 15), 566786, "Marketing", "Digital Marketing Specialist", LocalDate.of(2023, 2, 4), 500090L, "daviddavis@test.com");
            Candidate candidate28 = new Candidate("Emily", "Evans", LocalDate.of(2022, 3, 20), 987678, "Operations", "Supply Chain Manager", LocalDate.of(2023, 3, 5), 900114L, "emilyevans@test.com");
            Candidate candidate29 = new Candidate("Frank", "Foster", LocalDate.of(2021, 5, 6), 345123, "IT", "Systems Administrator", LocalDate.of(2023, 4, 6), 301010L, "frankfoster@test.com");
            Candidate candidate30 = new Candidate("Grace", "Garrett", LocalDate.of(2020, 10, 12), 765211, "Finance", "Tax Accountant", LocalDate.of(2023, 5, 7), 760020L, "gracegarrett@test.com");
            Candidate candidate31 = new Candidate("Henry", "Harrison", LocalDate.of(2019, 7, 17), 123458, "Marketing", "Brand Manager", LocalDate.of(2023, 6, 8), 120020L, "henryharrison@test.com");
            Candidate candidate32 = new Candidate("Isabella", "Ives", LocalDate.of(2022, 3, 5), 567099, "Operations", "Operations Specialist", LocalDate.of(2023, 7, 9), 509890L, "isabellaives@test.com");
            Candidate candidate33 = new Candidate("James", "Johnson", LocalDate.of(2021, 5, 2), 927683, "IT", "Network Engineer", LocalDate.of(2023, 8, 10), 909884L, "jamesjohnson@test.com");

            candidateService.saveCandidate(candidate);
            candidateService.saveCandidate(candidate2);
            candidateService.saveCandidate(candidate3);
            candidateService.saveCandidate(candidate04);
            candidateService.saveCandidate(candidate05);
            candidateService.saveCandidate(candidate06);
            candidateService.saveCandidate(candidate07);
            candidateService.saveCandidate(candidate08);
            candidateService.saveCandidate(candidate09);
            candidateService.saveCandidate(candidate10);
            candidateService.saveCandidate(candidate11);
            candidateService.saveCandidate(candidate12);
            candidateService.saveCandidate(candidate13);
            candidateService.saveCandidate(candidate14);
            candidateService.saveCandidate(candidate15);
            candidateService.saveCandidate(candidate16);
            candidateService.saveCandidate(candidate17);
            candidateService.saveCandidate(candidate18);
            candidateService.saveCandidate(candidate19);
            candidateService.saveCandidate(candidate20);
            candidateService.saveCandidate(candidate21);
            candidateService.saveCandidate(candidate22);
            candidateService.saveCandidate(candidate23);
            candidateService.saveCandidate(candidate24);
            candidateService.saveCandidate(candidate25);
            candidateService.saveCandidate(candidate26);
            candidateService.saveCandidate(candidate27);
            candidateService.saveCandidate(candidate28);
            candidateService.saveCandidate(candidate29);
            candidateService.saveCandidate(candidate30);
            candidateService.saveCandidate(candidate31);
            candidateService.saveCandidate(candidate32);
            candidateService.saveCandidate(candidate33);

            // print out just for me, this can be removed later
            System.out.println(candidate);
            System.out.println(candidate2);
            System.out.println(candidate3);
       };


    }

}