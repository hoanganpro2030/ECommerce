package vn.elca.training.service;

import vn.elca.training.model.entity.ACMUser;
import vn.elca.training.model.exception.EmailExistException;
import vn.elca.training.model.exception.UserNameExistException;
import vn.elca.training.model.exception.UserNotFoundException;

import javax.mail.MessagingException;
import java.util.List;

public interface ACMUserService {
    ACMUser register(String fullName, String username, String email) throws UserNotFoundException, UserNameExistException, EmailExistException, MessagingException;

    List<ACMUser> getUsers();

    ACMUser findUserByUsername(String username);

    ACMUser findUserByEmail(String email);
}
