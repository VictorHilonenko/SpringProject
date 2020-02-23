package spring.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import spring.scheduler.dto.UserDTO;
import spring.scheduler.entity.EmailMessage;
import spring.scheduler.entity.User;
import spring.scheduler.entity.enums.Role;
import spring.scheduler.entity.enums.ServiceType;
import spring.scheduler.exception.ExceptionKind;
import spring.scheduler.exception.ExtendedRuntimeException;
import spring.scheduler.repository.UserRepository;
import spring.scheduler.util.UserAuthentication;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private EmailMessageService emailMessageService;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, EmailMessageService emailMessageService,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailMessageService = emailMessageService;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            log.error("exception during findByEmail: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }

    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            log.error("exception during getAllUsers: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }

    public List<User> getAllUsers(Sort sort) {
        try {
            return userRepository.findAll(sort);
        } catch (Exception e) {
            log.error("exception during getAllUsers: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }

    public User addUser(UserDTO userDTO) {
        User newUser = userFromDTO(userDTO);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        try {
            return userRepository.save(newUser);
        } catch (Exception e) {
            log.error("exception during addUser: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }

    public User updateUser(UserDTO userDTO) {
        User user = findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new ExtendedRuntimeException(ExceptionKind.USER_NOT_FOUND));

        try {
            user.setRole(Role.valueOf(userDTO.getRole()));
        } catch (Exception e) {
            log.error("exception during updateUser: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.WRONG_DATA_PASSED, e);
        }

        user.setServiceType(ServiceType.lookupNotNull(userDTO.getServiceType()));

        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("repository exception during updateUser: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }

        return user;
    }

    public boolean deleteUser(String email) {
        User user = findByEmail(email).orElseThrow(() -> new ExtendedRuntimeException(ExceptionKind.USER_NOT_FOUND));

        try {
            userRepository.delete(user);
        } catch (Exception e) {
            log.error("exception during deleteUser: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }

        return true;
    }

    public void deleteUser(UserDTO userDTO) {
        deleteUser(userDTO.getEmail());
    }

    public UserDTO userToDTO(User user) {
        return UserDTO.builder().id(user.getId()).email(user.getEmail()).firstName(user.getFirstName())
                .lastName(user.getLastName()).telNumber(user.getTelNumber()).role(user.getRole().name())
                .serviceType(Optional.ofNullable(user.getServiceType()).map(ServiceType::name).orElse("")).build();
    }

    private User userFromDTO(UserDTO userDTO) {
        return User.builder().id(userDTO.getId()).email(userDTO.getEmail()).firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName()).telNumber(userDTO.getTelNumber()).password(userDTO.getPassword())
                .role(Role.DEFAULT_ROLE).serviceType(ServiceType.lookupNotNull(userDTO.getServiceType())).build();
    }

    String getFullNamePlusTelNumber(User user) {
        return user.getFirstName() + " " + user.getLastName() + " \n(" + user.getTelNumber() + ")";
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email).orElseThrow(() -> new ExtendedRuntimeException(ExceptionKind.USER_NOT_FOUND));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                Collections.singletonList(user.getRole()));
    }

    String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private String getCurrentAuthority() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst().get()
                .getAuthority();
    }

    Role getCurrentRole() {
        return Role.valueOf(getCurrentAuthority());
    }

    public Optional<User> getCurrentUser() {
        try {
            return userRepository.findByEmail(getCurrentUserName());
        } catch (Exception e) {
            log.error("exception " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }
    }

    private void setAuthentication(String email) {
        User user = findByEmail(email).orElseThrow(() -> new ExtendedRuntimeException(ExceptionKind.USER_NOT_FOUND));

        SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(user));
    }

    void setAuthenticationByQuickAccessCode(String quickAccessCode) {
        EmailMessage emailMessage = emailMessageService.findEmailMessageByQuickAccessCode(quickAccessCode)
                .orElseThrow(() -> new ExtendedRuntimeException(ExceptionKind.PAGE_NOT_FOUND));

        emailMessage.setQuickAccessCode("");

        try {
            emailMessageService.save(emailMessage);
        } catch (Exception e) {
            log.error("exception during emailMessageService.save: " + e.getMessage());
            throw new ExtendedRuntimeException(ExceptionKind.REPOSITORY_ISSUE, e);
        }

        try {
            setAuthentication(emailMessage.getEmail());
        } catch (Exception e) {
            log.error("exception during setAuthentication: " + e.getMessage());
        }
    }
}