package vn.elca.training.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.elca.training.model.dto.ACMUserDto;
import vn.elca.training.model.entity.ACMUser;
import vn.elca.training.model.entity.HttpResponse;
import vn.elca.training.model.exception.*;
import vn.elca.training.service.ACMUserService;
import vn.elca.training.service.impl.LoginAttemptService;
import vn.elca.training.util.JWTTokenProvider;
import vn.elca.training.util.UserMapper;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static vn.elca.training.constant.FileConstant.*;

@CrossOrigin(origins = "http://127.0.0.1:4200")
@RestController
@RequestMapping("/user")
public class UserController {
    public static final String EMAIL_SENT = "An email with a password was sent to: ";
    public static final String USER_DELETE_SUCCESSFULLY = "User delete successfully";
    private AuthenticationManager authenticationManager;
    private ACMUserService userService;
    private JWTTokenProvider jwtTokenProvider;
    private LoginAttemptService loginAttemptService;

    @Autowired
    public UserController(AuthenticationManager authenticationManager,
                                    ACMUserService userService, JWTTokenProvider jwtTokenProvider,
                                    LoginAttemptService loginAttemptService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginAttemptService = loginAttemptService;
    }

    @PostMapping("/create")
    public ResponseEntity<ACMUserDto> addNewUser(@RequestBody ACMUserDto userDto,
                                                 @RequestParam(required = false) MultipartFile profileImage) throws UserNotFoundException, EmailExistException, MessagingException, IOException, UserNameExistException, MissingInformationRequiredException {
        ACMUserDto newUserDto = userService.addNewUser(userDto, profileImage);
        return new ResponseEntity<>(newUserDto, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<ACMUserDto> updateUser(@RequestBody ACMUserDto userDto,
                                                 @RequestParam(required = false) MultipartFile profileImage) throws UserNotFoundException, EmailExistException, IOException, UserNameExistException, MissingInformationRequiredException {
        ACMUserDto updatedUserDto = userService.updateUser(userDto, profileImage);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<ACMUserDto> getUser(@PathVariable("username") String username) {
        ACMUser user = userService.findUserByUsername(username);
        ACMUserDto userDto = UserMapper.INSTANCE.ACMUserToACMUserDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ACMUserDto>> getAllUsers() {
        List<ACMUserDto> userDtos = userService.getAllUsers();
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/resetPassword/{uid}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable Long uid) throws MessagingException, UserNotFoundException {
        String email = userService.resetPassword(uid);
        return response(HttpStatus.OK, EMAIL_SENT + email);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return response(HttpStatus.NO_CONTENT, USER_DELETE_SUCCESSFULLY);
    }

    @PostMapping("/updateProfileImage")
    public ResponseEntity<ACMUserDto> updateUser(@RequestParam String username,
                                                 @RequestParam MultipartFile profileImage) throws UserNotFoundException, EmailExistException, IOException, UserNameExistException {
        ACMUserDto updatedUserDto = userService.updateProfileImage(username, profileImage);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    @GetMapping(path = "/image/{username}/{filename}", produces = IMAGE_JPEG_VALUE)
    public byte[] resetPassword(@PathVariable String username, @PathVariable String filename) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + filename + DOT + JPG_EXTENSION));
    }

    @GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable String username) throws IOException {
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int byteRead;
            byte[] chunk = new byte[1024];
            while ((byteRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, byteRead);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase()), httpStatus);
    }
}
