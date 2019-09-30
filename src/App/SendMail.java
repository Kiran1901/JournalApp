package App;

import com.sendgrid.*;

import java.io.IOException;

public class SendMail {

    public static void sendPlainMail() {
        Email from = new Email("kuldeepjadeja459@gmail.com");
        String subject = "Return the money";
        Email to = new Email("kiran.italiya19@gmail.com");
        String user = "Kiran Italiya";
        Content content = new Content("text/html", "<b>Hi , Mr. " + user + "</b>.<br> Greetings from Journal-App team .<br>Our user <b>Mr. Kuldeepsinh </b>has reported that you have to pay him <b>10 Rs. </b>as on 29/06/2019, so return it as soon as possible. <br> Regards ,<br><b>Team Journal-App</b>");
        Mail mail = new Mail(from, subject, to, content);
        String versiont = "d-0ffc3c3c50e743fc86e91dcec9ffdc84";
//        String versionm = "81cac53e-c3f6-4ebe-9281-9d0a82995d80";
        mail.setTemplateId(versiont);
        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}