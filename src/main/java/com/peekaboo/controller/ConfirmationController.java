package com.peekaboo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/registration")
public class ConfirmationController {

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public ResponseEntity confirm() {

    }

    private class ConfirmationResponse{

    }
}
