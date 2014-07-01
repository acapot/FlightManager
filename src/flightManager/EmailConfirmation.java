package flightManager;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class EmailConfirmation
{
  private static final String SMTP_HOST_NAME = "smtp.robertanderson.se"; //or simply "localhost"
  private static final String SMTP_AUTH_USER = "contact@robertanderson.se";
  private static final String SMTP_AUTH_PWD  = "nnnnnnnn";
  private static final String emailMsgTxt      = "Alright!";
  private static final String emailSubjectTxt  = "Ok! Alright!";
  private static final String emailFromAddress = "me@domain.com";

  // Add List of Email address to who email needs to be sent to
  private static List<String> emailList;

  public static void main(String args[]) throws Exception
  {
    EmailConfirmation smtpMailSender = new EmailConfirmation();
    emailList = new ArrayList<String>();
    emailList.add("ransv01@student.lnu.se");
    smtpMailSender.postMail( emailList, emailSubjectTxt, emailMsgTxt, emailFromAddress);
    System.out.println("Sucessfully Sent mail to All Users");
  }

  public void postMail( List<String> recipients, String subject,
	String message , String from) throws MessagingException, AuthenticationFailedException
  {
    boolean debug = false;

    //Set the host smtp address
    Properties props = new Properties();
    props.put("mail.smtp.host", SMTP_HOST_NAME);
    props.put("mail.smtp.auth", "true");
    Authenticator auth = new SMTPAuthenticator();
    Session session = Session.getDefaultInstance(props, auth);

    session.setDebug(debug);

    // create a message
    Message msg = new MimeMessage(session);

    // set the from and to address
    InternetAddress addressFrom = new InternetAddress(from);
    msg.setFrom(addressFrom);

    InternetAddress[] addressTo = new InternetAddress[recipients.size()];
    for (int i = 0; i < recipients.size(); i++)
    {
        addressTo[i] = new InternetAddress(recipients.get(i));
    }
    msg.setRecipients(Message.RecipientType.TO, addressTo);

    // Setting the Subject and Content Type
    msg.setSubject(subject);
    msg.setContent(message, "text/plain");
    Transport.send(msg);
 }

private class SMTPAuthenticator extends javax.mail.Authenticator
{
    public PasswordAuthentication getPasswordAuthentication()
    {
        String username = SMTP_AUTH_USER;
        String password = SMTP_AUTH_PWD;
        return new PasswordAuthentication(username, password);
    }
}
}