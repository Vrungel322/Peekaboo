package com.peekaboo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class ConfirmationController {

    @RequestMapping(value = "/confirm/{id}")
    public String confirm(@PathVariable String id) {
        return null;
    }
}
