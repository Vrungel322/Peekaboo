package com.peekaboo.controller;

import com.peekaboo.controller.helper.*;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.UserRole;
import com.peekaboo.model.entity.VerificationToken;
import com.peekaboo.model.service.UserService;
import com.peekaboo.model.service.VerificationTokenService;
import com.peekaboo.registrconfirm.ConfirmEvent;
import com.peekaboo.registrconfirm.RegistrationConfirmPublisher;
import com.peekaboo.registrconfirm.mail.MailService;
import com.peekaboo.security.jwt.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@RestController
public class SignController {

    private final Logger logger = LogManager.getLogger(SignController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenService tokenService;

    @Autowired
    private RegistrationConfirmPublisher registrationPublisher;

    @Autowired
    private MailService mailService;

    @Autowired
    private JwtUtil jwtUtil;


    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity signin(@Valid SigninRequestEntity requestEntity, Errors errors) throws Exception {
        logger.debug("Got SIGN IN request");
        if (errors.hasErrors()) {
            logErrors(errors);

            return new ResponseEntity(
                    transformErrors(errors.getAllErrors()),
                    HttpStatus.BAD_REQUEST
            );
        }
        User user = userService.findByLogin(requestEntity.getEmail());
        if (user == null || !user.getPassword().equals(requestEntity.getPassword())) {
            logger.debug("User has entered wrong login or password. Sending NOT_FOUND response");
            return new ResponseEntity(
                    new ErrorResponse(ErrorType.WRONG_LOGIN_OR_PASSWORD, "User entered wrong login or password"),
                    HttpStatus.NOT_FOUND
            );
        }
        logger.debug("User were successfully authorized");
        SignResponse response = new SignResponse();
        response.setId(user.getId())
                .setUsername(user.getUsername())
                .setRole(user.getRoles());
        String token = jwtUtil.generateToken(response);

        return new ResponseEntity(token, HttpStatus.OK);
    }

    //todo: after Roma's commit handle errors
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity signup(@Valid SignupRequestEntity requestEntity, Errors errors) {
        logger.debug("Got SIGN UP request");
        if (errors.hasErrors()) {
            logErrors(errors);
            return new ResponseEntity(
                    transformErrors(errors.getAllErrors()),
                    HttpStatus.BAD_REQUEST
            );
        }
        //todo: check uniqueness
        User user = userService.findByLogin(requestEntity.getLogin());
        if (user != null && user.isEnabled()) {
            logger.debug("User already exists. Sending BAD_REQUEST status");
            return new ResponseEntity(
                    new ErrorResponse(ErrorType.USER_EXIST, "User already exists"),
                    HttpStatus.BAD_REQUEST
            );
        } else {
            if (!user.isEnabled()) {
                tokenService.deleteByValue(tokenService.findByUser(user).getValue());
            } else {
                user = new User();
                user.setLogin(requestEntity.getLogin());
                user.setPassword(requestEntity.getPassword());
                user.addRole(UserRole.USER);
                user = userService.create(user);
            }
            //TODO: generate token ('test')!
            VerificationToken verToken = tokenService.create(new VerificationToken("test", user));
            //TODO: choose way of sending by login
            registrationPublisher.publishEvent(new ConfirmEvent(this, user, verToken, mailService));
        }

        SignResponse response = new SignResponse();
        response.setId(user.getId())
                .setUsername(user.getUsername())
                .setRole(user.getRoles());

        //TODO: delete token from sign up
        logger.debug("User were successfully created");
        String token = jwtUtil.generateToken(response);

        return new ResponseEntity(token, HttpStatus.OK);
    }

    private void logErrors(Errors errors) {
        logger.info("User has entered invalid data.");
        errors.getAllErrors().forEach(objectError -> {
            logger.debug(objectError.getObjectName() + " : " + objectError.getDefaultMessage());
        });
    }

    private List<ErrorResponse> transformErrors(List<ObjectError> errors) {
        return errors.stream()
                .map(objectError ->
                        new ErrorResponse(
                                ErrorType.AUTHENTICATION_ERROR,
                                objectError.getDefaultMessage()
                        )
                )
                .collect(Collectors.toList());
    }
}
