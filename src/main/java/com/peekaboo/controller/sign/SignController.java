package com.peekaboo.controller.sign;

import com.peekaboo.confirmation.RegistrationConfirmService;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import com.peekaboo.model.entity.enums.UserRole;
import com.peekaboo.model.service.impl.UserServiceImpl;
import com.peekaboo.model.service.impl.VerificationTokenServiceImpl;
import com.peekaboo.security.jwt.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/")
public class SignController {

    private final Logger logger = LogManager.getLogger(this);
    @Autowired
    UserServiceImpl userService;
    @Autowired
    VerificationTokenServiceImpl verificationService;
    @Autowired
    RegistrationConfirmService registrationConfirmService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    BCryptPasswordEncoder encoder;

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity signin(@RequestBody SigninRequestEntity requestEntity, Errors errors) throws Exception {
        logger.debug("Got SIGN IN request");
        if (errors.hasErrors()) {
            logErrors(errors);
            return new ResponseEntity(
                    transformErrors(errors.getAllErrors()),
                    HttpStatus.BAD_REQUEST
            );
        }
        User user;
        if (!requestEntity.isEmail() && !requestEntity.isPhone()) {
            user = userService.findByUsername(requestEntity.getLogin());
        } else {
            user = userService.findByLogin(requestEntity.getLogin());
        }
        String password = requestEntity.getPassword();

        if (user == null || !encoder.matches(password, user.getPassword())) {
            logger.debug("User has entered wrong login or password. Sending NOT_FOUND response");
            return new ResponseEntity(
                    new ErrorResponse(ErrorType.WRONG_LOGIN_OR_PASSWORD, "User entered wrong login or password"),
                    HttpStatus.NOT_FOUND
            );
        }
        logger.debug("User were successfully authorized");
        SignResponse response = new SignResponse();
        response.setId(user.getId().toString())
                .setUsername(user.getUsername())
                .setRole(user.getRoles())
                .setEnabled(true)
                .setState(user.getState());
        String token = jwtUtil.generateToken(response);
        logger.error(token);
        return new ResponseEntity(new SigninResponse(user.getId().toString(), token, user.getUsername() ,user.getState()), HttpStatus.OK);
//        return new ResponseEntity(new SigninResponse(user.getId().toString(),
//                user.getUsername().toString(),user.getAvatar().toString(), token), HttpStatus.OK);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity signup(@RequestBody SignupRequestEntity requestEntity, Errors errors) {
        logger.debug("Got SIGN UP request");
        logger.debug("Attempting to register new user");
        if (errors.hasErrors()) {
            logErrors(errors);
            return new ResponseEntity(
                    transformErrors(errors.getAllErrors()),
                    HttpStatus.BAD_REQUEST
            );
        }
        User user = userService.findByUsername(requestEntity.getUsername());
        String password = encoder.encode(requestEntity.getPassword());
        if (user != null) {
            logger.debug("User has registered before");
            logger.debug("Checking maybe he hasn't been verified yet");
            if (user.isEnabled()) {
                logger.debug("User has already been registered. Send him error");
                return new ResponseEntity(
                        new ErrorResponse(ErrorType.USER_EXIST, "Username is taken"),
                        HttpStatus.CONFLICT
                );
            }
            if (user.hasLogin(requestEntity.getLogin()) ||
                    !userService.loginExists(requestEntity.getLogin())) {

                logger.debug("User has entered unique login or login belongs to him. Updating user info");
                user.emptyLogin();
                user.setLogin(requestEntity.getLogin());
                user.setPassword(password);
                userService.update(user);
                logger.debug("Removing old verification key");
                verificationService.deleteByValue(verificationService.findByUser(user).getValue());

            } else {
                logger.debug("Login is taken");
                return new ResponseEntity(
                        new ErrorResponse(ErrorType.USER_EXIST, "Login is taken"),
                        HttpStatus.CONFLICT
                );
            }

        } else {
            if (userService.loginExists(requestEntity.getLogin())) {
                logger.debug("Login is taken");
                return new ResponseEntity(
                        new ErrorResponse(ErrorType.USER_EXIST, "Login is taken"),
                        HttpStatus.CONFLICT
                );
            } else {
                user = new User();
                user.setUsername(requestEntity.getUsername());
                user.setname(user.getUsername());
                user.setLogin(requestEntity.getLogin());
                user.setPassword(password);
                user.setEnabled(true);
                user.addRole(UserRole.USER);
                user = userService.create(user);
            }
        }
        VerificationToken verToken = registrationConfirmService.generateVerificationToken();
        verToken.setUser(user);
        verToken = verificationService.create(verToken);
        registrationConfirmService.confirm(user, verToken);
        SignupResponse response = new SignupResponse(user.getId().toString());
//        SignupResponse response = new SignupResponse(user.getId().toString(), user.getUsername().toString(), user.getAvatar().toString());
        logger.debug("User were successfully created");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    private void logErrors(Errors errors) {
        logger.info("User has entered invalid data.");
        for (ObjectError error : errors.getAllErrors()) {
            logger.debug(error.toString());
        }
    }

    private List<ErrorResponse> transformErrors(List<ObjectError> errors) {
        List<ErrorResponse> errorResponses = new ArrayList<>();
        for (ObjectError error : errors) {
            errorResponses.add(
                    new ErrorResponse(
                            ErrorType.AUTHENTICATION_ERROR,
                            error.getDefaultMessage()
                    )
            );
        }
        return errorResponses;
    }

    private class SigninResponse {
        private String username;
        private String id;
        private String token;
        private int state;
//        private String username;
//        private String avatar;

        //        public SigninResponse(String id, String username, String avatar, String token) {
        public SigninResponse(String id, String token, String username, int state) {
            this.token = token;
            this.id = id;
            this.state = state;

            this.username = username;
//            this.avatar = avatar;
        }

        public String getId() {
            return id;
        }

        public String getToken() {
            return token;
        }

        public int getState() {
            return state;
        }
//        public String getAvatar() { return avatar; }
//        public String getUsername() { return username; }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    private class SignupResponse {
        private String id;
//        private String username;
//        private String avatar;

        public SignupResponse(String id) {
//        public SignupResponse(String id, String username, String avatar) {
            this.id = id;
//            this.avatar = avatar;
//            this.username = username;
        }

        public String getId() {
            return id;
        }
//        public String getUsername() { return username;}
//        public String getAvatar() {return avatar;}
    }


}
