package com.peekaboo.controller;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        logger.error("trying to get all Users");
        logger.error(users.size());
        if (users.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Gson gson=new Gson();
            String s=gson.toJson(users);
            Map<String,String> m=new HashMap<String,String>();
            logger.error("Attention");
            UsersListResponse response = new UsersListResponse(users);
            logger.error("Success");
            return new ResponseEntity<>(response,HttpStatus.OK);
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

        public UsersListResponse(ArrayList<User> users) {
            this.usersList = new ArrayList<>();

                this.usersList.addAll(users);

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
    private final Logger logger = LogManager.getLogger(this);
}
