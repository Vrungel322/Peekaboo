package com.peekaboo.confirmation;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;

public class ConfirmAction extends Thread {
    private final User user;
    private final VerificationToken verificationToken;
    private final ConfirmSender confirmSender;

    public ConfirmAction(User user, VerificationToken token, ConfirmSender sender) {
        this.user = user;
        this.verificationToken = token;
        this.confirmSender = sender;
    }

    @Override
    public void run() {
        confirmSender.send(user.getLogin(), body0+verificationToken.getValue()+body1);
    }

    public User getUser() {
        return user;
    }

    public VerificationToken getVerificationToken() {
        return verificationToken;
    }

    public String body0 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
            "    <title>Demystifying Email Design</title>\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
            "</head>\n" +
            "<body style=\"margin: 0; padding: 0;\">\n" +
            "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border-collapse: collapse;font-family: Gadugi, sans-serif;\">\n" +
            "    <tr>\n" +
            "        <td align=\"center\" bgcolor=\"#f1f1f1\" style=\"padding: 40px 0 30px 0;\">\n" +
            "\n" +
            "            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
            "                <tr>\n" +
            "                    <td align=\"center\" style=\"padding: 0 0 10px 0px\">\n" +
            "                        <img src=\"https://www.peekaboochat.com/icons/pb.png\" alt=\"PeekabooLogo\" width=\"40\" height=\"40\" style=\"display:block\"/>\n" +
            "                    </td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td style=\"padding:30px 100px 20px 100px; font-size: 28px; text-align: center;\">\n" +
            "                        Welcome to Peekaboo\n" +
            "                    </td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td>\n" +
            "                        <table border=\"0\" align=\"center\" bgcolor=\"#ffffff\" cellpadding=\"0\" cellspacing=\"0\" width=\"400\"\n" +
            "                                style=\"border-radius: 6px\">\n" +
            "                            <tr>\n" +
            "                                <td style=\"padding:20px 20px 0 20px;\" >\n" +
            "                                    Please, confirm your registration with this code:\n" +
            "                                </td>\n" +
            "                            </tr>\n" +
            "                            <tr>\n" +
            "                                <td align=\"center\" style=\"padding: 10px 20px 10px 20px; color: #00c9a8; font-size: 24px\">";

    public String body1 = "</td>\n" +
            "                            </tr>\n" +
            "                            <tr>\n" +
            "                                <td style=\"padding: 0 20px 20px 20px\">\n" +
            "                                    Thank you!\n" +
            "                                </td>\n" +
            "                            </tr>\n" +
            "                        </table>\n" +
            "                    </td>\n" +
            "                </tr>\n" +
            "            </table>\n" +
            "        </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td bgcolor=\"#303030\" style=\"color: #cacaca\">\n" +
            "            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
            "                <tr>\n" +
            "                    <td align=\"center\" style=\"padding: 30px 0 0 0\">\n" +
            "                        <img src=\"https://www.peekaboochat.com/icons/peekaboo.png\" width=\"154\" height=\"32\" alt=\"Peekaboo\"/>\n" +
            "                    </td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td align=\"center\" style=\"padding: 30px 0 25px 0px\">\n" +
            "                        <a href=\"http://www.w3schools.com\"><img src=\"https://www.peekaboochat.com/icons/facebook.png\" alt=\"Facebook\" width=\"50\" height=\"50\" style=\"display:inline-block; padding: 0 15px 0 15px\"/></a>\n" +
            "                        <a href=\"http://www.w3schools.com\"><img src=\"https://www.peekaboochat.com/icons/twitter.png\" alt=\"Twitter\" width=\"50\" height=\"50\" style=\"display:inline-block; padding: 0 15px 0 15px\"/></a>\n" +
            "                        <a href=\"http://www.w3schools.com\"><img src=\"https://www.peekaboochat.com/icons/in.png\" alt=\"LinkedIn\" width=\"50\" height=\"50\" style=\"display:inline-block; padding: 0 15px 0 15px\"/></a>\n" +
            "                        <a href=\"http://www.w3schools.com\"><img src=\"https://www.peekaboochat.com/icons/insta.png\" alt=\"Instagram\" width=\"50\" height=\"50\" style=\"display:inline-block;padding: 0 15px 0 15px\"/></a>\n" +
            "                    </td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td align=\"center\">\n" +
            "                        &copy;2016 Canyon Capital LLC. All rights reserved.\n" +
            "                    </td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td align=\"center\" style=\"padding:0 0 30px 0;\">\n" +
            "                        640 Don Nicolas Road, Taos, New Mexico 87571, USA\n" +
            "                    </td>\n" +
            "                </tr>\n" +
            "            </table>\n" +
            "        </td>\n" +
            "    </tr>\n" +
            "\n" +
            "</table>\n" +
            "</body>\n" +
            "</html>\n";

}