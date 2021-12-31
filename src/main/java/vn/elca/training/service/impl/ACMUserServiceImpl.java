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
import vn.elca.training.model.exception.*;
import vn.elca.training.repository.ACMUserRepository;
import vn.elca.training.service.ACMUserService;
import vn.elca.training.service.EmailService;
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
import static vn.elca.training.constant.ValidatorConstant.MISSING_INFORMATION_REQUIRED;
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
    public ACMUser register(ACMUserDto userDto) throws UserNotFoundException, UserNameExistException, EmailExistException, MessagingException {
        validateNewUsernameAndEmail(EMPTY, userDto.getUsername(), userDto.getEmail());
        ACMUser user = new ACMUser();
        user.setUserId(generateUserId());

        user.setVerificationCode(generateVerificationCode());
        user.setFullName(userDto.getFullName());
        user.setUsername(userDto.getUsername());
        user.setPassword(encodePassword(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setJoinDate(new Date());
        user.setActive(false);
        user.setNotLocked(true);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(userDto.getUsername()));
        acmUserRepository.save(user);
//        log.info("New user password: " + password);
        emailService.sendVerificationEmail(userDto.getFullName(), user.getVerificationCode(), userDto.getEmail());
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
    public ACMUser findUserByUserId(Long userId) {
        return acmUserRepository.findOne(userId);
    }

    @Override
    public ACMUser findUserByEmail(String email) {
        return acmUserRepository.findACMUserByEmail(email);
    }

    @Override
    public ACMUserDto addNewUser(ACMUserDto userDto, MultipartFile profileImage) throws UserNotFoundException, UserNameExistException, EmailExistException, MessagingException, IOException, MissingInformationRequiredException {
        validateNewUsernameAndEmail(EMPTY, userDto.getUsername(), userDto.getEmail());
        String password = generateVerificationCode();
        String encodedPassword = encodePassword(password);
        ACMUser user = UserMapper.INSTANCE.ACMUserDtoToACMUser(userDto);
        user.setUserId(generateUserId());
        user.setJoinDate(new Date());
        if (userDto.getRole() == null) {
            throw new MissingInformationRequiredException(MISSING_INFORMATION_REQUIRED + "Role");
        }
        user.setRole(getRoleEnumName(userDto.getRole()).name());
        user.setAuthorities(getRoleEnumName(userDto.getRole()).getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(userDto.getUsername()));
        user.setPassword(encodedPassword);
        acmUserRepository.save(user);
        saveProFileImage(user, profileImage);
        emailService.sendVerificationEmail(user.getFullName(), user.getPassword(), user.getEmail());
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
    public ACMUserDto updateUser(ACMUserDto userDto, MultipartFile profileImage) throws UserNotFoundException, UserNameExistException, EmailExistException, IOException, MissingInformationRequiredException {
        if ( userDto.getId() == null) {
           throw new MissingInformationRequiredException(MISSING_INFORMATION_REQUIRED + "user id");
        }
        ACMUser user = acmUserRepository.findOne(userDto.getId());
        if (user == null) {
            throw new UserNotFoundException(NO_USER_FOUND_BY_ID + userDto.getId());
        }
        if ( userDto.getUsername() != null ) {
            validateNewUsernameAndEmail(EMPTY, userDto.getUsername(), EMPTY);
            user.setUsername( userDto.getUsername() );
        }
        if ( userDto.getEmail() != null ) {
            validateNewUsernameAndEmail(EMPTY, EMPTY, userDto.getEmail());
            user.setEmail( userDto.getEmail() );
        }
        if ( userDto.getRole() != null) {
            user.setRole(getRoleEnumName(userDto.getRole()).name());
            user.setAuthorities(getRoleEnumName(userDto.getRole()).getAuthorities());
        }
        if ( userDto.getIsActive() != null ) {
            user.setActive( userDto.getIsActive() );
        }
        if ( userDto.getIsNotLocked() != null ) {
            user.setNotLocked( userDto.getIsNotLocked() );
        }
        if ( userDto.getFullName() != null ) {
            user.setFullName( userDto.getFullName() );
        }
        saveProFileImage(user, profileImage);
        acmUserRepository.save(user);
        return UserMapper.INSTANCE.ACMUserToACMUserDto(user);
    }

    @Override
    public void resetPassword(String email) throws MessagingException, EmailNotFoundExeption {
        ACMUser user = acmUserRepository.findACMUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundExeption(NO_USER_FOUND_BY_EMAIL + email);
        }
        String password = generateVerificationCode();
        user.setPassword(encodePassword(password));
        acmUserRepository.save(user);
        emailService.sendVerificationEmail(user.getFullName(), password, user.getEmail());
    }

    public void verifyNewUserAccount(String verificationCode, String email) throws EmailNotFoundExeption {
        ACMUser user = acmUserRepository.findACMUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundExeption(NO_USER_FOUND_BY_EMAIL + email);
        }
        if (verificationCode.equals(user.getVerificationCode())) {
            user.setActive(true);
        }
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

    private String generateVerificationCode() {
        return RandomStringUtils.randomAlphanumeric(20);
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
