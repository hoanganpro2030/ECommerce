package vn.elca.training.service;

import org.springframework.web.multipart.MultipartFile;
import vn.elca.training.model.dto.ACMUserDto;
import vn.elca.training.model.entity.ACMUser;
import vn.elca.training.model.exception.EmailExistException;
import vn.elca.training.model.exception.EmailNotFoundExeption;
import vn.elca.training.model.exception.UserNameExistException;
import vn.elca.training.model.exception.UserNotFoundException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface ACMUserService {
    ACMUser register(String fullName, String username, String email) throws UserNotFoundException, UserNameExistException, EmailExistException, MessagingException;

    List<ACMUserDto> getAllUsers();

    void deleteUser(long id);

    ACMUser findUserByUsername(String username);

    ACMUser findUserByEmail(String email);

    ACMUserDto addNewUser(ACMUserDto user, MultipartFile profileImage) throws UserNotFoundException, UserNameExistException, EmailExistException, MessagingException, IOException;

    ACMUserDto updateUser(ACMUserDto userDto, MultipartFile profileImage) throws UserNotFoundException, UserNameExistException, EmailExistException, IOException;

    void resetPassword(String email) throws EmailNotFoundExeption, MessagingException;

    ACMUserDto updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UserNameExistException, EmailExistException, IOException;
}
