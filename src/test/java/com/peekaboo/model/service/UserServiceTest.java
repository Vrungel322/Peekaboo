//package com.peekaboo.model.service;
//
//import com.peekaboo.model.entity.User;
//import com.peekaboo.model.service.impl.UserServiceImpl;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.junit.Assert;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.ArrayList;
//
////import java.time.LocalDate;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"/config/root.xml"})
//public class UserServiceTest {
//    @Test
//    public void Test(){
//        UserServiceImpl a=new UserServiceImpl();
//        User u=a.findByLogin("login");
//        Assert.assertEquals(u,null);
//    }
//
//}
////
//
//
////        User user = new User();
////
////        user.setUsername("John");
//////        user.setLastName("Doe");
////        user.setLogin("john@gmail.com");
////        user.setPassword("qwerty");
//////        user.setTelephone("0954624456");
//////        user.setBirthdate(LocalDate.now());
//////        user.setGender(1);
////        user = userService.create(user);
////        User another = new User();
//////
////
////
////        Assert.assertEquals("User " + user.getUsername() + " should be equal to himself", another, userService.get(user.getId()));
////
////    }
////
////    @Test(expected = NullPointerException.class)
////    public void testException() {
////        doSomething();
////    }
////
////    private void doSomething() {
////        throw new IllegalArgumentException();
////    }
////
////}