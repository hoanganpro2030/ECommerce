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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.elca.training.model.dto.ACMUserDto;
import vn.elca.training.model.entity.ACMUser;
import vn.elca.training.model.entity.UserPrincipal;
import vn.elca.training.model.enumeration.Role;
import vn.elca.training.model.exception.EmailExistException;
import vn.elca.training.model.exception.EmailNotFoundExeption;
import vn.elca.training.model.exception.UserNameExistException;
import vn.elca.training.model.exception.UserNotFoundException;
import vn.elca.training.repository.ACMUserRepository;
import vn.elca.training.service.ACMUserService;
import vn.elca.training.service.EmailService;
import vn.elca.training.util.MapService;
import vn.elca.training.util.UserMapper;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static vn.elca.training.constant.FileConstant.*;
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
        user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
        acmUserRepository.save(user);
        log.info("New user password: " + password);
        emailService.sendNewPasswordEmail(fullName, password, email);
        return user;
    }

    @Override
    public List<ACMUserDto> getAllUsers() {
        List<ACMUser> userList = acmUserRepository.findAll();
        return UserMapper.INSTANCE.listACMUserToListACMUserDto(userList);
    }

    @Override
    public void deleteUser(long id) {
        acmUserRepository.delete(id);
    }

    @Override
    public ACMUser findUserByUsername(String username) {
        return acmUserRepository.findACMUserByUsername(username);
    }

    @Override
    public ACMUser findUserByEmail(String email) {
        return acmUserRepository.findACMUserByEmail(email);
    }

    @Override
    public ACMUserDto addNewUser(ACMUserDto userDto, MultipartFile profileImage) throws UserNotFoundException, UserNameExistException, EmailExistException, MessagingException, IOException {
        validateNewUsernameAndEmail(EMPTY, userDto.getUsername(), userDto.getEmail());
        String password = generatePassword();
        String encodedPassword = encodePassword(password);
        ACMUser user = UserMapper.INSTANCE.ACMUserDtoToACMUser(userDto);
        user.setUserId(generateUserId());
        user.setJoinDate(new Date());
        user.setRole(getRoleEnumName(userDto.getRole()).name());
        user.setAuthorities(getRoleEnumName(userDto.getRole()).getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(userDto.getUsername()));
        user.setPassword(encodedPassword);
        acmUserRepository.save(user);
        saveProFileImage(user, profileImage);
        emailService.sendNewPasswordEmail(user.getFullName(), user.getPassword(), user.getEmail());
        return UserMapper.INSTANCE.ACMUserToACMUserDto(user);
    }

    private void saveProFileImage(ACMUser user, MultipartFile profileImage) throws IOException {
        if (profileImage != null) {
            Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                log.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(user.getUsername()));
            acmUserRepository.save(user);
            log.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(USER_IMAGE_PATH + username + FORWARD_SLASH + username + DOT + JPG_EXTENSION).toUriString();
    }

    @Override
    public ACMUserDto updateUser(ACMUserDto userDto, MultipartFile profileImage) throws UserNotFoundException, UserNameExistException, EmailExistException, IOException {
        validateNewUsernameAndEmail(EMPTY, userDto.getUsername(), userDto.getEmail());
        ACMUser user = acmUserRepository.findOne(userDto.getId());
        if (user == null) {
            throw new UserNotFoundException(NO_USER_FOUND_BY_ID + userDto.getId());
        }
        if (userDto.getAuthorities() != null && userDto.getAuthorities().length != 0) {
            user.setAuthorities(getRoleEnumName(userDto.getRole()).getAuthorities());
        }
        if ( userDto.getRole() != null) {
            user.setRole(getRoleEnumName(userDto.getRole()).name());
        }
        if ( userDto.getIsActive() != null ) {
            user.setActive( userDto.getIsActive() );
        }
        if ( userDto.getIsNotLocked() != null ) {
            user.setNotLocked( userDto.getIsNotLocked() );
        }
        if ( userDto.getUsername() != null ) {
            user.setUsername( userDto.getUsername() );
        }
        if ( userDto.getFullName() != null ) {
            user.setFullName( userDto.getFullName() );
        }
        if ( userDto.getEmail() != null ) {
            user.setEmail( userDto.getEmail() );
        }
        saveProFileImage(user, profileImage);
        acmUserRepository.save(user);
        return UserMapper.INSTANCE.ACMUserToACMUserDto(user);
    }

    @Override
    public void resetPassword(String email) throws EmailNotFoundExeption, MessagingException {
        ACMUser user = acmUserRepository.findACMUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundExeption(NO_USER_FOUND_BY_EMAIL + email);
        }
        String password = generatePassword();
        user.setPassword(encodePassword(password));
        acmUserRepository.save(user);
        emailService.sendNewPasswordEmail(user.getFullName(), password, email);
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    @Override
    public ACMUserDto updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UserNameExistException, EmailExistException, IOException {
        ACMUser user = validateNewUsernameAndEmail(username, null, null);
        saveProFileImage(user, profileImage);
        return UserMapper.INSTANCE.ACMUserToACMUserDto(user);
    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
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
