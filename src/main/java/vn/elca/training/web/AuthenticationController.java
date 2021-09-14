package vn.elca.training.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import vn.elca.training.model.dto.ACMUserDto;
import vn.elca.training.model.dto.PurchaseOrderDto;
import vn.elca.training.model.exception.NotEnoughProductQuantityException;
import vn.elca.training.model.response.MessageReponse;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(origins = "http://127.0.0.1:4200")
@RestController
@RequestMapping("/user")
public class AuthenticationController extends ApiExceptionHandler{
    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/login")
    public void login() throws HttpRequestMethodNotSupportedException {
        throw new HttpRequestMethodNotSupportedException("");
    }
}
