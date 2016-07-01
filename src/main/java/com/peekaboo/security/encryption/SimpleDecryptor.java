package com.peekaboo.security.encryption;

import org.springframework.stereotype.Component;

@Component
public class SimpleDecryptor implements Decryptor {
    @Override
    public String decrypt(String data) {
        StringBuilder sb = new StringBuilder();
        data.chars().map(operand -> (operand+1)).forEach(ch -> sb.append((char)ch));
        return sb.toString();
    }
}
