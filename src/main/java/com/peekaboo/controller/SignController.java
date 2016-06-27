package com.peekaboo.controller;

import com.peekaboo.controller.helper.SignResponse;
import com.peekaboo.controller.helper.SigninRequestEntity;
import com.peekaboo.controller.helper.SignupRequestEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity signin(@Valid SigninRequestEntity requestEntity, Errors errors) {
        logger.debug("Got SIGN IN request");
        if (errors.hasErrors()) {
            logErrors(errors);
            return new ResponseEntity(errors.getAllErrors().toArray(), HttpStatus.BAD_REQUEST);
        }

        if (requestEntity.getUsername().equals(requestEntity.getPassword())) {
            logger.debug("User has entered wrong login or password. Sending NOT_FOUND response");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            SignResponse response = new SignResponse();
            response.setId("1");
            ResponseEntity responseEntity = new ResponseEntity(response, HttpStatus.OK);
            return responseEntity;
        }
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity signup(@Valid SignupRequestEntity requestEntity, Errors errors) {
        logger.debug("Got SIGN UP request");
        if (errors.hasErrors()) {
            logErrors(errors);
            return new ResponseEntity(errors.getAllErrors().toArray(), HttpStatus.BAD_REQUEST);
        }

        //temporal solution
        if (requestEntity.getUsername().equals(requestEntity.getPassword())) {
            logger.debug("User has entered wrong login or password. Sending NOT_FOUND response");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        SignResponse response = new SignResponse();
        response.setId("1");

        return new ResponseEntity(response, HttpStatus.OK);

    }

    private void logErrors(Errors errors) {
        logger.info("User has entered invalid data.");
        errors.getAllErrors().forEach(objectError -> {
            logger.debug(objectError.getObjectName() + " : " + objectError.getDefaultMessage());
        });
    }


}
