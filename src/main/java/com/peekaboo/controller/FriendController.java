package com.peekaboo.controller;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FriendController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/friend/find", method = RequestMethod.GET)
    public ResponseEntity<FriendResponse> findByUsername(FriendRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user != null) {
            FriendResponse response = new FriendResponse(user.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public static class FriendRequest {
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public static class FriendResponse {
        private String id;

        public FriendResponse(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

}
