package flightManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Flight {
	private int id;
	private Route route;
	private Airplane airplane;
	private Date departure;
	private int totalCost;
	private int capacity;
	private int bookedTickets;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPrice() {
		totalCost = airplane.getKilometerCost() * route.getDistance() / 1000;
		return totalCost;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public Airplane getAirplane() {
		return airplane;
	}

	public void setAirplane(Airplane airplane) {
		this.airplane = airplane;
	}

	public Date getDeparture() {
		return departure;
	}

	public String getDepartureString() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(departure);
	}

	public void setDeparture(Date departure) {
		this.departure = departure;
	}
	
	public int getCapacity() {
		return capacity;
	}

	public int getBookedTickets() {
		return bookedTickets;
	}

	public Flight(int id, Route route, Airplane airplane,
			Date departure) {
		super();
		this.id = id;
		this.route = route;
		this.airplane = airplane;
		this.departure = departure;
		this.capacity = airplane.getfCapacity() + airplane.getsCapacity() + airplane.gettCapacity();
		this.bookedTickets = DB.getBookedTickets(DB.getConnection(), this);
	}

	public Flight(Route route, Airplane airplane, Date departure) {
		super();
		this.route = route;
		this.airplane = airplane;
		this.departure = departure;
		this.capacity = airplane.getfCapacity() + airplane.getsCapacity() + airplane.gettCapacity();
	}

	public int getTravelTime() {
		return (route.getDistance() / airplane.getVelocity());

	}

	public Date getArrival() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(departure);
		cal.add(Calendar.MINUTE, getTravelTime());
		return cal.getTime();
	}

	@Override
	public String toString() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return "Flight #" + id + ": " + df.format(getDeparture()) + " " + route.toString() ;
	}
}
