package com.fon.service;


import com.fon.config.EmailParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
@Slf4j
public class EmailService {
    @Autowired
    private EmailParams emailParams;

    public void sendEmail(String text, String emailTo) {

        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", emailParams.getSMTP_SERVER());
        prop.put("mail.smtp.port", emailParams.getSMTP_PORT());
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.auth", "true");


        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(emailParams.getUSERNAME(), emailParams.getPASSWORD());

            }

        });
//        session.setDebug(true);

        try {

            MimeMessage msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(emailParams.getEMAIL_FROM()));

            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailTo, false));

            msg.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(emailParams.getEMAIL_TO_CC(), false));

            msg.setSubject(emailParams.getEMAIL_SUBJECT());

            msg.setText(text);

            msg.setSentDate(new Date());

            Transport.send(msg, msg.getAllRecipients());

            log.info("Status slanja e-maila: uspesan; >> " + emailTo);

        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }

    }
}
