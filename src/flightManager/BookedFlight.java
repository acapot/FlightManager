package flightManager;

import java.util.Date;

public class BookedFlight {
	private int id;
	private Flight flight;
	private User user;
	private Date dateOfBooking;
	private int ticketCount;
	private int totalPrice;
	private boolean confirmed;
	private boolean cancelled;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDateOfBooking() {
		return dateOfBooking;
	}

	public void setDateOfBooking(Date dateOfBooking) {
		this.dateOfBooking = dateOfBooking;
	}

	public int getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(int ticketCount) {
		this.ticketCount = ticketCount;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public boolean getCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public BookedFlight(int id, Flight flight, User user, Date dateOfBooking, int ticketCount,
			int totalPrice, boolean confirmed, boolean cancelled) {
		super();
		this.id = id;
		this.flight = flight;
		this.user = user;
		this.dateOfBooking = dateOfBooking;
		this.ticketCount = ticketCount;
		this.totalPrice = totalPrice;
		this.confirmed = confirmed;
		this.cancelled = cancelled;
	}

	public BookedFlight(Flight flight, User user, Date dateOfBooking, int ticketCount,
			int totalPrice, boolean confirmed, boolean cancelled) {
		super();
		this.flight = flight;
		this.user = user;
		this.dateOfBooking = dateOfBooking;
		this.ticketCount = ticketCount;
		this.totalPrice = totalPrice;
		this.confirmed = confirmed;
		this.cancelled = cancelled;
	}
	@Override
	public String toString(){
		String out = "";
		out += "Flight #" + flight.getId() + " - " + flight.getRoute().toString() + " - " + flight.getDeparture() + " - Owner: " + this.user.toString() + ", Tickets: " + this.ticketCount;
		return out;
	}
}
