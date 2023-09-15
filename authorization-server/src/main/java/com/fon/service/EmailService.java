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

    public void sendEmail(String email, String link) {

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

        try {

            String htmlContent = generateEmailBody(link);

            MimeMessage msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(emailParams.getEMAIL_FROM()));

            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email, false));

            msg.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(emailParams.getEMAIL_TO_CC(), false));

            msg.setSubject(emailParams.getEMAIL_SUBJECT());

            msg.setContent(htmlContent, "text/html; charset=UTF-8");

            msg.setSentDate(new Date());

            Transport.send(msg, msg.getAllRecipients());

            log.info("Konfirmacioni email uspesno poslat na: " + email);

        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String generateEmailBody(String link) {

        return "<h2>STAMBENI OGLASNIK</h2>\n" +
                "<p>Potvrdite Vašu email adresu klikom na sledeći link:</p>\n" +
                "<a href=\"" + link + "\">" + link + "</a>";
    }
}
