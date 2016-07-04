package com.peekaboo.mail;

public interface Sender {

    void send(String to, String subject, String body);
}
