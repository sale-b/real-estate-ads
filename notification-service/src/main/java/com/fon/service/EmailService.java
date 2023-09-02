package com.fon.service;


import com.fon.config.EmailParams;
import com.fon.entity.Filter;
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

    public void sendEmail(RealEstate realEstate, Filter filter) {

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

            String htmlContent = generateEmailBody(realEstate, filter);

            MimeMessage msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(emailParams.getEMAIL_FROM()));

            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(filter.getUserEmail(), false));

            msg.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(emailParams.getEMAIL_TO_CC(), false));

            msg.setSubject(emailParams.getEMAIL_SUBJECT());

            msg.setContent(htmlContent, "text/html; charset=UTF-8");

            msg.setSentDate(new Date());

            Transport.send(msg, msg.getAllRecipients());

            log.info("Email notifikacija uspesno poslata na: " + filter.getUserEmail());

        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String generateEmailBody(RealEstate realEstate, Filter filter) {
        StringBuilder htmlContent = new StringBuilder();

        htmlContent.append("<h2>STAMBENI OGLASNIK</h2>\n" +
                "<p>Pronadjen je novi oglas za filter pod nazivom: <b>" + filter.getTitle() + "</b></p>\n" +
                "<div style=\"width: 50%;\">");
        htmlContent.append("<table style=\"font-family: arial, sans-serif;border-collapse: collapse;width: 400px;\">");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;background-color: #4CAF50;color: white;\"><b>" + realEstate.getTitle() + "</b></td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Datum objave: </b>" + realEstate.getCreatedOn().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "</td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Lokacija: </b>" + String.format("%s - %s - %s", realEstate.getLocation().getCityRegion().getCity().getName(), realEstate.getLocation().getCityRegion().getName(), realEstate.getLocation().getName()) + "</td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Tip oglasa: </b>" + realEstate.getRealEstateType().getLabel() + "</td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Tip nekretnine: </b>" + realEstate.getAdType().getLabel() + "</td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Kvadratura: </b>" + realEstate.getLivingSpaceArea() + "m<sup>2</sup></td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Broj soba: </b>" + realEstate.getRoomsNumber().getLabel() + "</td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Sprat: </b>" + realEstate.getFloor().getLabel() + "</td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Nameštenost: </b>" + realEstate.getFurnitureType().getLabel() + "</td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Telefon: </b><a href=\"tel:" + realEstate.getPhone() + "\">" + realEstate.getPhone() + "</a></td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\"><b>Cena: </b>" + String.format("%.0f", realEstate.getPrice()) + " &euro;</td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;background-color: #4CAF50;color: white;\"><b>Opis:</b></td></tr>");
        htmlContent.append(" <tr><td style=\"border: 1px solid #dddddd;text-align: left;padding: 8px;\">" + realEstate.getDescription() + "</td></tr>");
        htmlContent.append("</table>");

        return htmlContent.toString();
    }
}
