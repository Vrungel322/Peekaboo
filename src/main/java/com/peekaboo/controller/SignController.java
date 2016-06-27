package com.peekaboo.controller;

import com.peekaboo.controller.helper.SigninRequestEntity;
import com.peekaboo.controller.helper.SignupRequestEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
public class SignController {

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity signin(@Valid SigninRequestEntity requestEntity, Errors errors) {

        if (errors.hasErrors()) {
            return new ResponseEntity(errors.getAllErrors().toArray(), HttpStatus.BAD_REQUEST);
        }

        if (requestEntity.getUsername().equals(requestEntity.getPassword())) {
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
        if (errors.hasErrors()) {
            return new ResponseEntity(errors.getAllErrors().toArray(), HttpStatus.BAD_REQUEST);
        }

        //temporal solution
        if (requestEntity.getUsername().equals(requestEntity.getPassword())) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        SignResponse response = new SignResponse();
        response.setId("1");

        return new ResponseEntity(response, HttpStatus.OK);

    }



    private class SignResponse {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
