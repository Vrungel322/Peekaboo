package com.peekaboo.controller.settings;

import com.peekaboo.confirmation.RegistrationConfirmService;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import com.peekaboo.model.service.UserService;
import com.peekaboo.model.service.impl.VerificationTokenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SettingsController {
    @Autowired
    RegistrationConfirmService registrationConfirmService;
    @Autowired
    VerificationTokenServiceImpl verificationService;
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/settings/{userId}", method = RequestMethod.POST)
    public void setSettings(@RequestBody SettingsRequestEntity requestEntity, @PathVariable String userId) {
        try {
            User user = userService.get(userId);

            if (!user.getEmail().equals(requestEntity.getEmail()) && requestEntity.getEmail() != null) {
                user.setEmail(requestEntity.getEmail());
                VerificationToken verToken = registrationConfirmService.generateVerificationToken();
                verToken.setUser(user);
                verToken = verificationService.create(verToken);
                registrationConfirmService.confirm(user, verToken);
            }

            if (!user.getTelephone().equals(requestEntity.getPhone()) && requestEntity.getPhone() != null) {
                user.setTelephone(requestEntity.getPhone());
                VerificationToken verToken = registrationConfirmService.generateVerificationToken();
                verToken.setUser(user);
                verToken = verificationService.create(verToken);
                registrationConfirmService.confirm(user, verToken);
            }

            user.setName(Helper.check(requestEntity.getFirstName(), user.getName()));

            user.setSurname(Helper.check(requestEntity.getLastName(), user.getSurname()));

            user.setCountry(Helper.check(requestEntity.getCountry(), user.getCountry()));

            user.setCity(Helper.check(requestEntity.getCity(), user.getCity()));

            userService.update(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class Helper {
    public static String check(String value, String oldValue) {
        if (!oldValue.equals(value) && value != null) {
            return value;
        }

        return oldValue;
    }
}
