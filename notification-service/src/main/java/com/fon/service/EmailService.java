package com.fon.service;


import com.fon.config.EmailParams;
import com.fon.entity.Filter;
import com.fon.entity.Notification;
import com.fon.entity.RealEstate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;

@Service
@Slf4j
public class EmailService {
    @Autowired
    private EmailParams emailParams;

    public void sendEmail(Notification notification) {

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

            String htmlContent = generateEmailBody(notification);

            MimeMessage msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(emailParams.getEMAIL_FROM()));

            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(notification.getFilter().getUserEmail(), false));

            msg.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(emailParams.getEMAIL_TO_CC(), false));

            msg.setSubject(emailParams.getEMAIL_SUBJECT());

            msg.setContent(htmlContent, "text/html; charset=UTF-8");

            msg.setSentDate(new Date());

            Transport.send(msg, msg.getAllRecipients());

            log.info("Email notifikacija uspesno poslata na: " + notification.getFilter().getUserEmail());

        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String generateEmailBody(Notification notification) {
        StringBuilder htmlContent = new StringBuilder();

        htmlContent.append("<h2>STAMBENI OGLASNIK</h2>\n" +
                "<p>Pronadjen je novi oglas za filter pod nazivom: <b>" + notification.getFilter().getTitle() + "</b></p>\n" +
                "<a href=\"http://client-server:8081/ad/" + notification.getRealEstate().getId() + "\">VIDI OGLAS</a>\n" +
                "<div style=\"width: 50%;\">");
        htmlContent.append("<table style=\"font-family: arial, sans-serif;border-collapse: collapse;width: 400px;\">");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;background-color: #4CAF50;color: white;\"><b>" + notification.getRealEstate().getTitle() + "</b></td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Datum objave: </b>" + notification.getRealEstate().getCreatedOn().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "</td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Lokacija: </b>" + String.format("%s - %s - %s", notification.getRealEstate().getLocation().getCityRegion().getCity().getName(), notification.getRealEstate().getLocation().getCityRegion().getName(), notification.getRealEstate().getLocation().getName()) + "</td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Tip oglasa: </b>" + notification.getRealEstate().getRealEstateType().getLabel() + "</td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Tip nekretnine: </b>" + notification.getRealEstate().getAdType().getLabel() + "</td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Kvadratura: </b>" + notification.getRealEstate().getLivingSpaceArea() + "m<sup>2</sup></td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Broj soba: </b>" + notification.getRealEstate().getRoomsNumber().getLabel() + "</td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Sprat: </b>" + notification.getRealEstate().getFloor().getLabel() + "</td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Nameštenost: </b>" + notification.getRealEstate().getFurnitureType().getLabel() + "</td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Telefon: </b><a href=\"tel:" + notification.getRealEstate().getPhone() + "\">" + notification.getRealEstate().getPhone() + "</a></td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Cena: </b>" + String.format("%.0f", notification.getRealEstate().getPrice()) + " &euro;</td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;background-color: #4CAF50;color: white;\"><b>Opis:</b></td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + notification.getRealEstate().getDescription() + "</td></tr>");
        htmlContent.append("</table>");

        return htmlContent.toString();
    }
}
