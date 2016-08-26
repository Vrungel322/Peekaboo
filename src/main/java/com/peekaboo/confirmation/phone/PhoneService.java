package com.peekaboo.confirmation.phone;

import com.peekaboo.confirmation.ConfirmSender;
import org.springframework.stereotype.Service;

@Service
public class PhoneService implements ConfirmSender {

    @Override
    public void send(String to, String body) {

    }
}
