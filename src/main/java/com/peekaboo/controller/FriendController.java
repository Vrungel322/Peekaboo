package com.peekaboo.controller;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class FriendController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/friend/find", method = RequestMethod.GET)
    public ResponseEntity<FriendResponse> findFriendByUsername(FriendRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user != null) {
            FriendResponse response = new FriendResponse(user.getId().toString());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/allusers/find", method = RequestMethod.GET)
    public ResponseEntity<UsersListResponse> getAllUsersList() {
        ArrayList users = (ArrayList) userService.getAll();
        if (users.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            UsersListResponse response = new UsersListResponse(users);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/userbyid/find", method = RequestMethod.GET)
    public ResponseEntity<UserResponse> getUserFromDataBase(UserRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user != null) {
            UserResponse response = new UserResponse(user.getId().toString());
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

    public static class UsersListResponse {

        private ArrayList<User> usersList;

        public UsersListResponse(ArrayList users) {
            this.usersList = users;
        }

        public ArrayList<User> getUsersList() {
            return usersList;
        }
        public void setUsersList(ArrayList<User> usersList) {
            this.usersList = usersList;
        }
    }

    public static class UserRequest {
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public static class UserResponse {
        private String id;

        public UserResponse(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

}
