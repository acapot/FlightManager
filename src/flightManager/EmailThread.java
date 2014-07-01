package flightManager;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

public class EmailThread implements Runnable {
   
	private User user;
	private BookedFlight bookedFlight;
	private String subject;
	private String message;
	private String from;
	
	public static final int CANCELLED_FLIGHT = 1; 
	public static final int CONFIRMED_FLIGHT = 2; 
	public static final int CREATED_FLIGHT = 3; 
	public static final int UPDATED_FLIGHT = 4; 
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public EmailThread(User user, BookedFlight bookedFlight, int type) {
		super();
		this.user = user;
		this.bookedFlight = bookedFlight;
		this.from = "RobAnd@FlightManager.com";
		switch (type) {
		case CANCELLED_FLIGHT:	buildMailCancelled();	break;
		case CONFIRMED_FLIGHT:	buildMailConfirmed();	break;
		case CREATED_FLIGHT:	buildMailCreated();		break;
		case UPDATED_FLIGHT:	buildMailUpdated();		break;
		}
	}

	private void buildMailCancelled(){
		this.subject = "Ticket Booking #" + this.bookedFlight.getId() + " Cancelled";
		this.message = "Hello " + this.bookedFlight.getUser().toString() + "\nYour flight has been cancelled. \n\n Deal with it. :|";
	}
	private void buildMailConfirmed(){
		this.subject = "Ticket Booking #" + bookedFlight.getId() + " Confirmed";
		this.message = "Hello " + this.bookedFlight.getUser().toString() + "\nYour flight has been Confirmed. \n\n Enjoy your trip.";
	}
	private void buildMailCreated(){
		this.subject = "Ticket Booking #" + bookedFlight.getId() + " Registered";
		this.message = "Hello " + this.bookedFlight.getUser().toString() + "\nYour flight has been registered. \n\n Your order will be processed shortly.";
	}
	private void buildMailUpdated(){
		this.subject = "Ticket Booking #" + bookedFlight.getId() + " Updated";
		this.message = "Hello " + this.bookedFlight.getUser().toString() + "\nYour flight data has been changed. \n\n You should check it out.";
	}
	
	@Override
	public void run() {
		EmailConfirmation EC = new EmailConfirmation();
		List<String> recipients = new ArrayList<String>();
		recipients.add(user.getEmail());
		try {
			EC.postMail(recipients, subject, message, from);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
}