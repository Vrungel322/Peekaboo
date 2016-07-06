package com.peekaboo.registrconfirm.phone;

import com.peekaboo.registrconfirm.ConfirmSender;
import org.springframework.stereotype.Service;

@Service
public class PhoneService implements ConfirmSender {

    @Override
    public void send(String to, String body) {

    }
}
