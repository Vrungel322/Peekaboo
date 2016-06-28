package com.peekaboo.model.service;

import com.peekaboo.model.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/config/root.xml"})
public class UserServiceTest {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;


    @Test
    public void testAdd() {
        final User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("john");
        user.setPassword("qwert");
        user.setEmail("john@gmail.com");
        user.setTelephone("0954624456");
        user.setBirthdate(LocalDate.now());
        user.setGender(1);
        userService.add(user);
        Assert.assertEquals(user, userService.get(1l));
    }
}