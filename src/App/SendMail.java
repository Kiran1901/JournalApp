package App;

import com.sendgrid.*;
import org.apache.commons.codec.binary.Base64;
//import javax.mail.internet;
//import javax.mail.Message;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SendMail {

    public static void sendMail(String user,String pEmail,String pName,int amount,String date) {
        Email from = new Email("kuldeepjadeja459@gmail.com");
        String subject = "Return the money";
        Email to = new Email(pEmail);
//        String user = "Kiran Italiya";

        Content content = new Content("text/html","<b>Hi , Mr. " + user + "</b>.<br> Greetings from Journal-App team .<br>Our user <b>"+user+"</b>has reported that you have to pay him <b>"+amount+" </b>as on "+date+", so return it as soon as possible. <br> Regards ,<br><b>Team Journal-App</b>");
        Mail mail = new Mail(from, subject, to, content);
//        String versiont = "d-0ffc3c3c50e743fc86e91dcec9ffdc84";
//        String versionm = "81cac53e-c3f6-4ebe-9281-9d0a82995d80";
//        mail.setTemplateId(versiont);
//        SendGrid sg = new SendGrid("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");
//        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
        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println("Body  "+response.getBody());
            System.out.println("Headers  "+response.getHeaders());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}