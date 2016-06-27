package com.peekaboo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignController {

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity<SignResponse> signin(@RequestParam String username, @RequestParam String password) {
        if (username.equals(password)) {
            return new ResponseEntity<SignResponse>(HttpStatus.NOT_FOUND);
        } else {
            SignResponse response = new SignResponse();
            response.setId("1");
            ResponseEntity<SignResponse> responseEntity = new ResponseEntity<SignResponse>(response, HttpStatus.OK);
            return responseEntity;
        }
    }


    @RequestMapping(method = RequestMethod.GET)
    public SignResponse hello() {
        SignResponse sr = new SignResponse();
        sr.setId("hello world");
        return sr;
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
