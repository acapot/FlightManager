package flightManager;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlightManager {
	public static User activeUser;
	public List<Airport> airports = new ArrayList<Airport>();
	public List<City> cities = new ArrayList<City>();
	public List<Flight> flights = new ArrayList<Flight>();
	public List<User> users = new ArrayList<User>();
	public List<Route> routes = new ArrayList<Route>();
	public List<Airplane> airplanes = new ArrayList<Airplane>();
	public List<BookedFlight> bookedFlights = new ArrayList<BookedFlight>();
	
	public void updateLists(){
		Connection conn = DB.getConnection(Globals.SERVER_URL, Globals.SERVER_USERNAME, Globals.SERVER_PASSWORD);
		this.airplanes = DB.getAirplanes(conn);
		this.airports = DB.getAirports(conn);
		this.cities = DB.getCities(conn);
		this.flights = DB.getFlights(conn);
		this.users = DB.getUsers(conn);
		this.routes = DB.getRoutes(conn);
	}
	
	public int findById(Airport e){
		for (int i = 0; i < airports.size(); i++) {
			if (e.getId() == airports.get(i).getId()) {
				return i;
			}
		}
		return -1;
	}
	public int findById(City e){
		for (int i = 0; i < cities.size(); i++) {
			if (e.getId() == cities.get(i).getId()) {
				return i;
			}
		}
		return -1;
	}
	public int findById(Flight e){
		for (int i = 0; i < flights.size(); i++) {
			if (e.getId() == flights.get(i).getId()) {
				return i;
			}
		}
		return -1;
	}
	public int findById(Route e){
		for (int i = 0; i < routes.size(); i++) {
			if (e.getId() == routes.get(i).getId()) {
				return i;
			}
		}
		return -1;
	}
	public int findById(Airplane e){
		for (int i = 0; i < airplanes.size(); i++) {
			if (e.getId() == airplanes.get(i).getId()) {
				return i;
			}
		}
		return -1;
	}
	public int findById(User e){
		for (int i = 0; i < users.size(); i++) {
			if (e.getId() == users.get(i).getId()) {
				return i;
			}
		}
		return -1;
	}
	public int findById(BookedFlight e){
		for (int i = 0; i < bookedFlights.size(); i++) {
			if (e.getId() == bookedFlights.get(i).getId()) {
				return i;
			}
		}
		return -1;
	}
	
	public int findAirplane(Airplane a){
		for (int i = 0; i < airplanes.size(); i++) {
			if (a.toString().equals(airplanes.get(i).toString())) {
				return i;
			}
		}
		return -1;
	}
	
	public int findRoute(Route r){
		for (int i = 0; i < routes.size(); i++) {
			if (r.getStart().getId() == routes.get(i).getStart().getId() && r.getDestination().getId() == routes.get(i).getDestination().getId()) {
				return i;
			}
		}
		return -1;
	}
	public List<Route> findRoutesByCities(City start, City dest){
		List<Route> out = new ArrayList<Route>();
		for (int i = 0; i < routes.size(); i++) {
			if (start.getId() == routes.get(i).getStart().getCity().getId() && dest.getId() == routes.get(i).getDestination().getCity().getId()) {
				out.add(routes.get(i));
			}
		}
		return out;
	}
	public int findUsername(String s){
		for (int i = 0; i < users.size(); i++) {
			if (s.equals(users.get(i).getUserName())) {
				return i;
			}
		}
		return -1;
	}
	
	public List<Flight> filterFlightsByArrival(List<Flight> list, Date min, Date max){
		//TODO TEST THIS!! - should return a list with all flights that are between the two dates
		List<Flight> flights = new ArrayList<Flight>();
		for (int i = 0; i < list.size(); i++) {
			if((list.get(i).getArrival().after(min) || min == null) && (list.get(i).getArrival().before(max) || max == null)){
				flights.add(list.get(i));
			}
		}
		return flights;
	}
	public List<Flight> filterFlightsByPrice(List<Flight> list, int maxPrice){
		//TODO TEST THIS!! - should return a list with all flights that are cheaper than the maxPrice
		List<Flight> flights = new ArrayList<Flight>();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getPrice() <= maxPrice){
				flights.add(list.get(i));
			}
		}
		return flights;
	}
	
	public FlightManager(){
		updateLists();
	}
}
