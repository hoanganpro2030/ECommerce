package vn.elca.training.service;

import org.springframework.web.multipart.MultipartFile;
import vn.elca.training.model.dto.ACMUserDto;
import vn.elca.training.model.entity.ACMUser;
import vn.elca.training.model.exception.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface ACMUserService {
    ACMUser register(ACMUserDto userDto) throws UserNotFoundException, UserNameExistException, EmailExistException, MessagingException;

    List<ACMUserDto> getAllUsers();

    void deleteUser(long id);

    ACMUser findUserByUsername(String username);

    ACMUser findUserByUserId(Long userId);

    ACMUser findUserByEmail(String email);

    ACMUserDto addNewUser(ACMUserDto user, MultipartFile profileImage) throws UserNotFoundException, UserNameExistException, EmailExistException, MessagingException, IOException, MissingInformationRequiredException;

    ACMUserDto updateUser(ACMUserDto userDto, MultipartFile profileImage) throws UserNotFoundException, UserNameExistException, EmailExistException, IOException, MissingInformationRequiredException;

    void resetPassword(String email) throws MessagingException, EmailNotFoundExeption;

    void verifyNewUserAccount(String verificationCode, String email) throws EmailNotFoundExeption;

    ACMUserDto updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UserNameExistException, EmailExistException, IOException;
}
