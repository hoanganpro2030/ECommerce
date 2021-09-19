package vn.elca.training.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.elca.training.model.entity.ACMUser;
import vn.elca.training.model.entity.UserPrincipal;
import vn.elca.training.model.exception.EmailExistException;
import vn.elca.training.model.exception.UserNameExistException;
import vn.elca.training.model.exception.UserNotFoundException;
import vn.elca.training.repository.ACMUserRepository;
import vn.elca.training.service.ACMUserService;
import vn.elca.training.service.EmailService;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static vn.elca.training.constant.UserImplConstant.*;
import static vn.elca.training.model.enumeration.Role.ROLE_USER;

@Service
@Transactional(rollbackFor = Throwable.class)
@Qualifier("UserDetailsService")
@Slf4j
public class ACMUserServiceImpl implements UserDetailsService, ACMUserService {
    @Autowired
    private ACMUserRepository acmUserRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ACMUser user = acmUserRepository.findACMUserByUsername(username);
        if (user == null) {
            log.error("User not found by user name: " + username);
            throw new UsernameNotFoundException(username);
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            acmUserRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            log.info("Returning found user by username");
            return userPrincipal;
        }
    }

    private void validateLoginAttempt(ACMUser user) {
        if (user.isNotLocked()) {
            if (loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
                user.setNotLocked(false);
            } else {
                user.setNotLocked(true);
            }
        } else {
            loginAttemptService.evictUserToLoginAttemptCache(user.getUsername());
        }
    }

    @Override
    public ACMUser register(String fullName, String username, String email) throws UserNotFoundException, UserNameExistException, EmailExistException, MessagingException {
        validateNewUsernameAndEmail(EMPTY, username, email);
        ACMUser user = new ACMUser();
        user.setUserId(generateUserId());
        String password = generatePassword();
        String encodedPassword = encodePassword(password);
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setPassword(encodedPassword);
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl());
        acmUserRepository.save(user);
        log.info("New user password: " + password);
        emailService.sendNewPasswordEmail(fullName, password, email);
        return user;
    }

    @Override
    public List<ACMUser> getUsers() {
        return acmUserRepository.findAll();
    }

    @Override
    public ACMUser findUserByUsername(String username) {
        return acmUserRepository.findACMUserByUsername(username);
    }

    @Override
    public ACMUser findUserByEmail(String email) {
        return acmUserRepository.findACMUserByEmail(email);
    }

    private String getTemporaryProfileImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH).toUriString();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private ACMUser validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UserNameExistException, EmailExistException {
        ACMUser userByNewUsername = findUserByUsername(newUsername);
        ACMUser userByNewEmail = findUserByEmail(newEmail);
        if(StringUtils.isNotBlank(currentUsername)) {
            ACMUser currentUser = findUserByUsername(currentUsername);
            if(currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UserNameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if(userByNewUsername != null) {
                throw new UserNameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }
}
