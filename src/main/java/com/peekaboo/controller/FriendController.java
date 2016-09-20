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
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.*;

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
    public ResponseEntity<UsersListResponse> getAllUsersList()  {
        ArrayList users = (ArrayList) userService.getAll();
        logger.error("trying to get all Users");
        logger.error(users.size());
        if (users.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {



            //todo:whether to show email?
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
    public static class StringResponse {
        private String str;
        public StringResponse(String s){
            this.str=s;
        }
        public String getString(){
            return this.str;
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

        private List<FriendEntity>  listFriends;

        public UsersListResponse(ArrayList<User> users)  {

            listFriends= new ArrayList();

            users.forEach(a->
                listFriends.add(new FriendEntity(a.getUsername(),a.getId())));
//            Gson gson=new Gson();
//            this.usersJsonArray=gson.toJson(listFriends);

        }


        public List<FriendEntity> getUsersList() {
            return listFriends;
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
