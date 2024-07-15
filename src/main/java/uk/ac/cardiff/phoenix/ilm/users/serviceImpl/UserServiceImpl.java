package uk.ac.cardiff.phoenix.ilm.users.serviceImpl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.users.model.User;
import uk.ac.cardiff.phoenix.ilm.users.model.RolePermission;
import uk.ac.cardiff.phoenix.ilm.users.model.UserRole;
import uk.ac.cardiff.phoenix.ilm.users.repository.UserRepository;
import uk.ac.cardiff.phoenix.ilm.security.PasswordUtil;
import uk.ac.cardiff.phoenix.ilm.users.service.UserService;

import java.util.List;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(RolePermissionServiceImpl.class);

    private final UserRepository userRepository;
    public UserServiceImpl( UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User getUserById(Long userID) {
        return userRepository.findById(userID).orElse(null);
    }

    @Override
    public User addUser(@NotNull User user) {
        if (getUserById(user.getId()) != null) {
            throw new EntityExistsException("User already exists");
        } else{
            return userRepository.save(user);
        }
    }

    @Override
    public User updateUser(@NotNull User user) {
        if (getUserById(user.getId()) == null) {
            throw new EntityNotFoundException("User does not exist");
        } else{
            return userRepository.save(user);
        }
    }

    @Override
    public void deleteUser(Long userID) {
        if (getUserById(userID) == null) {
            throw new EntityNotFoundException("User does not exist");
        } else{
            userRepository.deleteById(userID);
        }
    }

    @Override
    public List<User> getUsersByRole(String role) {
        // Implementation to get users by role using a query method
        return userRepository.findByRoles_Role(role);
    }

    @Override
    public void changePassword(@org.jetbrains.annotations.NotNull User user, String newPassword) {
        String salt = PasswordUtil.generateSalt();
        String hashedSaltedPassword = PasswordUtil.hashPassword(newPassword, salt);
        user.setSalt(salt);
        user.setPasswordHash(hashedSaltedPassword);
        userRepository.save(user);
    }

    @Override
    public void addRole(User user, UserRole role) {
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    public void removeRole(User user, UserRole role) {
        user.getRoles().remove(role);
        userRepository.save(user);
    }

    @Override
    //get all permissions for all roles which a user has
    public Set<RolePermission> getUserPermissions(User user) {
        return user.getRoles().stream()
                .map(UserRole::getPermissions)
                .flatMap(Set::stream)
                .collect(java.util.stream.Collectors.toSet());
    }


    @Override
    public boolean authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null){
            return false;
        }
        String salt = user.getSalt();
        String userHashedPassword = user.getPasswordHash();
        String hashedSaltedPassword = PasswordUtil.hashPassword(password, salt);
        return userHashedPassword.equals(hashedSaltedPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }



        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                getAuthorities(getUserPermissions(user))
        );
    }



    private Set<GrantedAuthority> getAuthorities(Set<RolePermission> roles) {
        return roles.stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role.getPermission())
                .collect(java.util.stream.Collectors.toSet());
    }



}
