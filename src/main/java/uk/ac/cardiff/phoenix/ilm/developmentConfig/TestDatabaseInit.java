package uk.ac.cardiff.phoenix.ilm.developmentConfig;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import uk.ac.cardiff.phoenix.ilm.programs.service.LevelsService;
import uk.ac.cardiff.phoenix.ilm.programs.service.WorkshopsService;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.service.CandidateService;
import uk.ac.cardiff.phoenix.ilm.users.model.User;
import uk.ac.cardiff.phoenix.ilm.users.model.RolePermission;
import uk.ac.cardiff.phoenix.ilm.users.model.UserRole;
import uk.ac.cardiff.phoenix.ilm.users.service.RolePermissionService;
import uk.ac.cardiff.phoenix.ilm.users.service.UserRoleService;
import uk.ac.cardiff.phoenix.ilm.users.service.UserService;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@Profile("test")
public class TestDatabaseInit {

    private final UserService userService;
    private final UserRoleService userRoleService;
    private final RolePermissionService rolePermissionService;
    private final LevelsService levelService;
    private final WorkshopsService workshopService;
    private final CandidateService candidateService;


    public TestDatabaseInit(UserService userService, UserRoleService userRoleService, RolePermissionService rolePermissionService, LevelsService levelsService, WorkshopsService workshopsService, CandidateService candidateService) {
        this.userRoleService = userRoleService;
        this.userService = userService;
        this.rolePermissionService = rolePermissionService;
        this.levelService = levelsService;
        this.workshopService = workshopsService;
        this.candidateService = candidateService;
    }


    @Bean
    public CommandLineRunner loadDevData() {
        return args -> {

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

        };
    }
}


