package com.peekaboo.controller;

import com.peekaboo.controller.helper.SignResponse;
import com.peekaboo.controller.helper.SigninRequestEntity;
import com.peekaboo.controller.helper.SignupRequestEntity;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.UserRole;
import com.peekaboo.model.service.UserService;
import com.peekaboo.security.jwt.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class SignController {

    final Logger logger = LogManager.getLogger(SignController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity signin(@Valid SigninRequestEntity requestEntity, Errors errors) throws Exception{

        logger.debug("Got SIGN IN request");
        if (errors.hasErrors()) {
            logErrors(errors);
            //todo: return errors in different way
            return new ResponseEntity(errors.getAllErrors().toArray(), HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByUsername(requestEntity.getUsername());
        if (user == null || !user.getPassword().equals(requestEntity.getPassword())) {
            logger.debug("User has entered wrong login or password. Sending NOT_FOUND response");
            throw new SignException();
        }

        SignResponse response = new SignResponse();
        response.setId(user.getId())
                .setUsername(user.getUsername())
                .setRole(user.getRole().toString());

        String token = jwtUtil.generateToken(response);

        return new ResponseEntity(token, HttpStatus.OK);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity signup(@Valid SignupRequestEntity requestEntity, Errors errors) {
        logger.debug("Got SIGN UP request");
        if (errors.hasErrors()) {
            logErrors(errors);
            return new ResponseEntity(errors.getAllErrors().toArray(), HttpStatus.BAD_REQUEST);
        }

        //todo: check uniqueness
        User newUser = new User();
        newUser.setUsername(requestEntity.getUsername());
        newUser.setEmail(requestEntity.getEmail());
        newUser.setPassword(requestEntity.getPassword());
        newUser.setRole(new UserRole(""));
        newUser = userService.add(newUser);

        SignResponse response = new SignResponse();
        response.setId(newUser.getId())
                .setUsername(newUser.getUsername())
                .setRole(newUser.getRole().toString());

        String token = jwtUtil.generateToken(response);

        return new ResponseEntity(token, HttpStatus.OK);

//        //temporal solution
//        if (requestEntity.getUsername().equals(requestEntity.getPassword())) {
//            logger.debug("User has entered wrong login or password. Sending NOT_FOUND response");
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
//        }
//
//        SignResponse response = new SignResponse();
//        response.setId("1");
//
//        return new ResponseEntity(response, HttpStatus.OK);


    }

    private void logErrors(Errors errors) {
        logger.info("User has entered invalid data.");
        errors.getAllErrors().forEach(objectError -> {
            logger.debug(objectError.getObjectName() + " : " + objectError.getDefaultMessage());
        });
    }

    @RequestMapping("/")
    public String hello() {
        return "Hello";
    }


}
