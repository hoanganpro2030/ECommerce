package vn.elca.training.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import vn.elca.training.model.dto.ACMUserDto;
import vn.elca.training.model.entity.ACMUser;
import vn.elca.training.model.entity.UserPrincipal;
import vn.elca.training.model.exception.EmailExistException;
import vn.elca.training.model.exception.UserNameExistException;
import vn.elca.training.model.exception.UserNotFoundException;
import vn.elca.training.service.ACMUserService;
import vn.elca.training.service.impl.LoginAttemptService;
import vn.elca.training.util.JWTTokenProvider;
import vn.elca.training.util.UserMapper;

import javax.mail.MessagingException;

import static org.springframework.http.HttpStatus.OK;
import static vn.elca.training.constant.SecurityConstant.JWT_TOKEN_HEADER;

@CrossOrigin(origins = "http://127.0.0.1:4200")
@RestController
@RequestMapping("/user")
public class AuthenticationController extends ApiExceptionHandler{
    private AuthenticationManager authenticationManager;
    private ACMUserService userService;
    private JWTTokenProvider jwtTokenProvider;
    private LoginAttemptService loginAttemptService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    ACMUserService userService, JWTTokenProvider jwtTokenProvider,
                                    LoginAttemptService loginAttemptService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginAttemptService = loginAttemptService;
    }

    @PostMapping("/login")
    public ResponseEntity<ACMUserDto> login(@RequestBody ACMUser user) {
        authenticate(user.getUsername(), user.getPassword());
        ACMUser loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        ACMUserDto acmUserDto = UserMapper.INSTANCE.ACMUserToACMUserDto(loginUser);
        return new ResponseEntity<>(acmUserDto, jwtHeader, OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ACMUser> register(@RequestBody ACMUserDto userDto) throws UserNotFoundException, UserNameExistException, EmailExistException, MessagingException {
        ACMUser newUser = userService.register(userDto);
        return new ResponseEntity<>(newUser, OK);
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        try {
            Object result = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            if (result instanceof UsernamePasswordAuthenticationToken) {
                loginAttemptService.evictUserToLoginAttemptCache(username);
            }
        } catch (BadCredentialsException e) {
            if (userService.findUserByUsername(username) != null) {
                loginAttemptService.addUserToLoginAttemptCache(username);
            }
            throw e;
        }
    }
}
