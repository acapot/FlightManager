package flightManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DB {
	public static Connection getConnection(String url, String user, String pass) {
		try {
			return DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Connection getConnection() {
		return getConnection(Globals.SERVER_URL, Globals.SERVER_USERNAME, Globals.SERVER_PASSWORD);
	}

	public static int checkUserLogin(Connection conn, String username,
			String password) {
		Statement st = null;
		ResultSet rs = null;
		String query = "SELECT id FROM users WHERE username = AES_ENCRYPT('" + username + "', '" + Globals.SERVER_URL + "')"
				+ " AND password = AES_ENCRYPT('" + password + "', '" + Globals.SERVER_URL + "')";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			if (rs.next() != false) {
				return rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static boolean changeUserPassword(Connection conn, int id,
			String pass) {
		Statement st = null;
		ResultSet rs = null;
		String query = "UPDATE users SET password = AES_ENCRYPT('" + pass + "', '" + Globals.SERVER_URL + "') ) WHERE id = "
				+ id;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			if (rs.next() != false) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
		}
		return false;
	}
	public static User getUser(Connection conn, int id) {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT id, AES_DECRYPT(username, '" + Globals.SERVER_URL + "') AS username, "
									+ "AES_DECRYPT(password, '" + Globals.SERVER_URL + "') AS password, "
									+ "userrank, AES_DECRYPT(email, '" + Globals.SERVER_URL + "') AS email "
									+ "FROM users WHERE id = " + id);
			if (rs.next()) {
				return buildUser(conn, rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<User> getUsers(Connection conn) {
		List<User> users = new ArrayList<User>();
		Statement st = null;
		ResultSet rs = null;
		String query = "SELECT id, AES_DECRYPT(username, '" + Globals.SERVER_URL + "') AS username, "
				+ "AES_DECRYPT(password, '" + Globals.SERVER_URL + "') AS password, "
				+ "userrank, AES_DECRYPT(email, '" + Globals.SERVER_URL + "') AS email "
				+ "FROM users";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				users.add(buildUser(conn, rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
	public static List<User> getUsersByRank(Connection conn, int rank) {
		List<User> users = new ArrayList<User>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM users WHERE userrank = " + rank);
			while (rs.next()) {
				users.add(buildUser(conn, rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
	public static User buildUser(Connection conn, ResultSet rs) {
		try {
			User user = new User(rs.getInt("id"), rs.getString("username"),
					rs.getString("password"), rs.getInt("userrank"),
					rs.getString("email"));
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static User insertUser(Connection conn, User user) {
		Statement st = null;
		String query = "INSERT INTO users (username, password, userrank, email) VALUES(AES_ENCRYPT('"
				+ user.getUserName()	+ "', '" + Globals.SERVER_URL + "'), AES_ENCRYPT('"
				+ user.getPassword()	+ "', '" + Globals.SERVER_URL + "'), "
				+ user.getUserRank()	+  ", AES_ENCRYPT('"
				+ user.getEmail()		+ "', '" + Globals.SERVER_URL + "'))";
		User out = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);
			user.setId(id);
			out = user;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}
	public static User updateUser(Connection conn, User user) {
		Statement st = null;
		String query = "UPDATE users SET" 
						+ " username =  AES_ENCRYPT('" + user.getUserName()  + "', '" + Globals.SERVER_URL + "')"
						+ ", password = AES_ENCRYPT('" + user.getPassword() + "', '" + Globals.SERVER_URL + "')" 
						+ ", userrank = " + user.getUserRank() 
						+ ", email = AES_ENCRYPT('" + user.getEmail() + "', '" + Globals.SERVER_URL + "')"
						+ " WHERE id = " + user.getId();
		User out = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
			out = getUser(conn, user.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}
	public static boolean deleteUser(Connection conn, int id) {
		Statement st = null;
		ResultSet rs = null;
		String query = "DELETE FROM users WHERE id = " + id;
		String queryBookedFlight = "SELECT bookedFlightId FROM bookedFlights WHERE userId = " + id;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
			rs = st.executeQuery(queryBookedFlight);
			while(rs.next()){
				deleteBookedFlight(conn, rs.getInt("bookedFlightId"));
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Airport buildAirport(Connection conn, ResultSet rs) {
		try {
			Airport airport = new Airport(rs.getInt("airportId"),
					rs.getString("airportName"), getCity(conn,
							rs.getInt("cityId")));
			return airport;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Airport getAirport(Connection conn, int id) {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM airports WHERE airportId = "
					+ id);
			if (rs.next()) {
				return buildAirport(conn, rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<Airport> getAirports(Connection conn) {
		List<Airport> airports = new ArrayList<Airport>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM airports");
			while (rs.next()) {
				airports.add(buildAirport(conn, rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return airports;
	}
	public static List<Airport> getAirportsByCity(Connection conn, int cityId) {
		List<Airport> airports = new ArrayList<Airport>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM airports WHERE cityId = "
					+ cityId);
			while (rs.next()) {
				airports.add(buildAirport(conn, rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return airports;
	}
	public static Airport insertAirport(Connection conn, Airport airport) {
		Statement st = null;
		String query = "INSERT INTO airports (airportName, cityId) VALUES('"
				+ airport.getAirportName() + "', " + airport.getCity().getId()
				+ ")";
		Airport out = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);
			airport.setId(id);
			out = airport;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}
	public static Airport updateAirport(Connection conn, Airport airport) {
		Statement st = null;
		String query = "UPDATE airports SET airportName = '"
				+ airport.getAirportName() + "', cityId = "
				+ airport.getCity().getId() + " WHERE airportId = "
				+ airport.getId();
		Airport out = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
			out = getAirport(conn, airport.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}
	public static boolean deleteAirport(Connection conn, int index) {
		Statement st = null;
		String query = "DELETE FROM airports WHERE airportId = " + index;
		String queryRoutes = "SELECT routeId FROM routes WHERE (startAirport = " + index + " OR destinationAirport = " + index + ")";
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
			rs = st.executeQuery(queryRoutes);
			while (rs.next()) {
				deleteRoute(conn, rs.getInt("routeId"));
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static City getCity(Connection conn, int index) {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM cities WHERE cityId = " + index);
			if (rs.next()) {
				return buildCity(conn, rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static City buildCity(Connection conn, ResultSet rs) {
		try {
			City city = new City(rs.getInt("cityId"), rs.getString("cityName"));
			return city;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<City> getCities(Connection conn) {
		List<City> cities = new ArrayList<City>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM cities");
			while (rs.next()) {
				cities.add(buildCity(conn, rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cities;
	}
	public static City insertCity(Connection conn, City city) {
		Statement st = null;
		String query = "INSERT INTO cities (cityName) VALUES('"
				+ city.getCityName() + "')";
		City outCity = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);
			city.setId(id);
			outCity = city;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return outCity;
	}
	public static City updateCity(Connection conn, City city) {
		Statement st = null;
		String query = "UPDATE cities SET cityName = '" + city.getCityName()
				+ "' WHERE cityId = " + city.getId();
		City out = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
			out = getCity(conn, city.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}
	public static boolean deleteCity(Connection conn, int index) {
		Statement st = null;
		String query = "DELETE FROM cities WHERE cityId = " + index;
		String queryAirports = "SELECT airportId FROM airports WHERE cityId = " + index;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
			rs = st.executeQuery(queryAirports);
			while (rs.next()) {
				deleteAirport(conn, rs.getInt("airportId"));
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Flight buildFlight(Connection conn, ResultSet rs) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Flight flight = new Flight(rs.getInt("flightId"), getRoute(conn, rs.getInt("routeId")),
					getAirplane(conn, rs.getInt("airplaneId")), df.parse(rs
							.getString("departure")));
			return flight;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<Flight> getFlights(Connection conn) {
		List<Flight> flights = new ArrayList<Flight>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM flights");
			while (rs.next()) {
				flights.add(buildFlight(conn, rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flights;
	}
	public static Flight getFlight(Connection conn, int index) {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM flights WHERE flightId = "
					+ index);
			if (rs.next()) {
				return buildFlight(conn, rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<Flight> getFlightsByRoute(Connection conn, int id, boolean showHistory) {
		List<Flight> flights = new ArrayList<Flight>();
		Statement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM flights WHERE routeId = " + id;
		if(!showHistory){
			query += " AND departure >= NOW()";
		}
		query += " ORDER BY departure";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				flights.add(buildFlight(conn, rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flights;
	}
	public static List<Flight> getFlightsByRouteAndDate(Connection conn, int id, Date min, Date max) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Flight> flights = new ArrayList<Flight>();
		Statement st = null;
		ResultSet rs = null;
		String query = "SELECT * FROM flights WHERE routeId = " + id;
		if(min != null){
			query += " AND departure >= DATE('" + df.format(min) + "')";
		}
		if(max != null){
			query += " AND departure <= DATE_ADD('" + df.format(max) + "', INTERVAL 1 DAY)";
		}
		query += " ORDER BY departure";
		//System.out.println(query);
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				flights.add(buildFlight(conn, rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flights;
	}
	public static Flight insertFlight(Connection conn, Flight flight) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Statement st = null;
		String query = "INSERT INTO flights (routeId, airplaneId, departure) "
				+ "VALUES("
				+ flight.getRoute().getId()
				+ ", "
				+ flight.getAirplane().getId()
				+ ", '"
				+ df.format(flight.getDeparture())
				+ "')";
		Flight outFlight = null;
		try {
			st = conn.createStatement();
			//System.out.print(query);
			st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);
			flight.setId(id);
			outFlight = flight;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return outFlight;
	}
	public static Flight updateFlight(Connection conn, Flight flight) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Statement st = null;
		String query = "UPDATE flights SET routeId = "
				+ flight.getRoute().getId() + ", airplaneId = "
				+ flight.getAirplane().getId() + ", departure = '"
				+ df.format(flight.getDeparture()) + "' WHERE flightId = " + flight.getId();
		Flight out = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
			out = getFlight(conn, flight.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}
	public static boolean deleteFlight(Connection conn, int index) {
		Statement st = null;
		String query = "DELETE FROM flights WHERE flightId = " + index;
		String queryBookedFlight = "SELECT bookedFlightId FROM bookedFlights WHERE flightId = " + index;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
			rs = st.executeQuery(queryBookedFlight);
			while(rs.next()){
				deleteBookedFlight(conn, rs.getInt("bookedFlightId"));
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static int getBookedTickets(Connection conn, Flight f){
		int out = 0;
		Statement st = null;
		String sql = "SELECT ticketCount FROM bookedFlights WHERE flightId = " + f.getId();
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				out += rs.getInt("ticketCount");
			}
		} catch (SQLException e) {
		}
		return out;
	}
	
	public static Route buildRoute(Connection conn, ResultSet rs){
		try {
			Route route = new Route(rs.getInt("routeId"),
									getAirport(conn, rs.getInt("startAirport")),
									getAirport(conn, rs.getInt("destinationAirport")),
									rs.getInt("distance"));
			return route;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Route getRoute(Connection conn, int id){
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM routes WHERE routeId = "
					+ id);
			if (rs.next()) {
				return buildRoute(conn, rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Route insertRoute(Connection conn, Route route){
		Statement st = null;
		String query = "INSERT INTO routes (startAirport, destinationAirport, distance) "
				+ "VALUES("
				+ route.getStart().getId()
				+ ", "
				+ route.getDestination().getId()
				+ ", "
				+ route.getDistance()
				+ ")";
		Route out = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);
			route.setId(id);
			out = route;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
		
	}
	public static Route updateRoute(Connection conn, Route route){
		Statement st = null;
		String query = "UPDATE routes SET startAirport = "
				+ route.getStart().getId() + ", destinationAirport = "
				+ route.getDestination().getId() + ", distance = "
				+ route.getDistance() + " WHERE routeId = " + route.getId();
		Route out = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
			out = getRoute(conn, route.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}
	public static List<Route> getRoutes(Connection conn){
		List<Route> routes = new ArrayList<Route>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM routes");
			while (rs.next()) {
				routes.add(buildRoute(conn, rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return routes;
	}
	public static boolean deleteRoute(Connection conn, int id){
		Statement st = null;
		String query = "DELETE FROM routes WHERE routeId = " + id;
		String queryFlights = "SELECT flightId FROM flights WHERE routeId = " + id;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
			rs = st.executeQuery(queryFlights);
			while(rs.next()){
				deleteFlight(conn, rs.getInt("flightId"));
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Airplane buildAirplane(Connection conn, ResultSet rs){
		try {
			Airplane airplane = new Airplane(rs.getInt("airplaneId"),
												rs.getString("brand"),
												rs.getString("model"),
												rs.getInt("fCapacity"),
												rs.getInt("sCapacity"),
												rs.getInt("tCapacity"),
												rs.getInt("velocity"),
												rs.getInt("kilometerCost")
											);
			return airplane;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Airplane getAirplane(Connection conn, int id){
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM airplanes WHERE airplaneId = "
					+ id);
			if (rs.next()) {
				return buildAirplane(conn, rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Airplane insertAirplane(Connection conn, Airplane airplane){
		Statement st = null;
		String query = "INSERT INTO airplanes (brand, model, velocity, fcapacity, scapacity, tcapacity, kilometerCost) "
				+ "VALUES('"
				+ airplane.getBrand()
				+ "', '"
				+ airplane.getModel()
				+ "', "
				+ airplane.getVelocity()
				+ ", "
				+ airplane.getfCapacity()
				+ ", "
				+ airplane.getsCapacity()
				+ ", "
				+ airplane.gettCapacity()
				+ ", "
				+ airplane.getKilometerCost()
				+ ")";
		Airplane out = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);
			airplane.setId(id);
			out = airplane;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}
	public static Airplane updateAirplane(Connection conn, Airplane airplane){
		Statement st = null;
		String query = "UPDATE airplanes SET brand = '"
				+ airplane.getBrand() + "', model = '"
				+ airplane.getModel() + "', velocity = "
				+ airplane.getVelocity() + ", fcapacity = " 
				+ airplane.getfCapacity() + ", scapacity = " 
				+ airplane.getsCapacity() + ", tcapacity = "
				+ airplane.gettCapacity() + ", kilometerCost = "
				+ airplane.getKilometerCost() + " WHERE airplaneId = " + airplane.getId();
		//System.out.println(query);
		Airplane out = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
			out = getAirplane(conn, airplane.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}
	public static List<Airplane> getAirplanes(Connection conn){
		List<Airplane> airplanes = new ArrayList<Airplane>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM airplanes");
			while (rs.next()) {
				airplanes.add(buildAirplane(conn, rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return airplanes;
	}
	public static boolean deleteAirplane(Connection conn, int id){
		Statement st = null;
		String query = "DELETE FROM airplanes WHERE airplaneId = " + id;
		String queryFlights = "SELECT flightId FROM flights WHERE airplaneId = " + id;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
			rs = st.executeQuery(queryFlights);
			while(rs.next()){
				deleteFlight(conn, rs.getInt("flightId"));
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static BookedFlight getBookedFlight(Connection conn, int id){
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM bookedFlights WHERE bookedFlightId = "
					+ id);
			if (rs.next()) {
				return buildBookedFlight(conn, rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<BookedFlight> getBookedFlights(Connection conn){
		List<BookedFlight> bookedFlights = new ArrayList<BookedFlight>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM bookedFlights ORDER BY flightId ASC");
			while (rs.next()) {
				bookedFlights.add(buildBookedFlight(conn, rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookedFlights;
	}
	public static List<BookedFlight> getBookedFlightsByOwner(Connection conn, User u){
		List<BookedFlight> bookedFlights = new ArrayList<BookedFlight>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM bookedFlights WHERE userId = " + u.getId() + " ORDER BY flightId ASC");
			while (rs.next()) {
				bookedFlights.add(buildBookedFlight(conn, rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookedFlights;
	}
	public static List<BookedFlight> getBookedFlightsByFlight(Connection conn, Flight f){
		//TODO TEST THIS!! - Does not seem to work, for some reason. Need to investigate.
		List<BookedFlight> bookedFlights = new ArrayList<BookedFlight>();
		String query = "SELECT * FROM bookedFlights WHERE flightId = " + f.getId();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				bookedFlights.add(buildBookedFlight(conn, rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookedFlights;
	}
	public static BookedFlight buildBookedFlight(Connection conn, ResultSet rs){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			BookedFlight bf = new BookedFlight(rs.getInt("bookedFlightId"),
												getFlight(conn, rs.getInt("flightId")),
												getUser(conn, rs.getInt("userId")),
												df.parse(rs.getString("dateOfBooking")),
												rs.getInt("ticketCount"),
												rs.getInt("totalPrice"),
												rs.getBoolean("confirmed"),
												rs.getBoolean("cancelled")
											);
			return bf;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static BookedFlight insertBookedFlight(Connection conn, BookedFlight bf){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Statement st = null;
		String query = "INSERT INTO bookedFlights (userId, flightId, ticketCount, totalPrice, dateOfBooking, confirmed, cancelled) "
				+ "VALUES("
				+ bf.getUser().getId()
				+ ", "
				+ bf.getFlight().getId()
				+ ", "
				+ bf.getTicketCount()
				+ ", "
				+ bf.getTotalPrice()
				+ ", '"
				+ df.format(bf.getDateOfBooking())
				+ "', 0, 0)";
		BookedFlight outBF = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);
			bf.setId(id);
			outBF = bf;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return outBF;
	}
	public static BookedFlight updateBookedFlight(Connection conn, BookedFlight bf){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Statement st = null;
		String query = "UPDATE bookedFlights SET userId = " + bf.getUser().getId()
				+ ", flightId = " + bf.getFlight().getId() 
				+ ", ticketCount = " + bf.getTicketCount() 
				+ ", totalPrice = " + bf.getTotalPrice() 
				+ ", dateOfBooking = '" + df.format(bf.getDateOfBooking())
				+ "', confirmed = " + (bf.getConfirmed() ? 1 : 0)
				+ ", cancelled = " + (bf.getCancelled() ? 1 : 0)
				+ " WHERE bookedFlightId = " + bf.getId();
		BookedFlight out = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
			out = getBookedFlight(conn, bf.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}
	public static boolean confirmBookedFlight(Connection conn, int id){
		Statement st = null;
		String query = "UPDATE bookedFlights SET confirmed = 1 WHERE bookedFlightId = " + id;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean deleteBookedFlight(Connection conn, int id){
		Statement st = null;
		String query = "DELETE FROM bookedFlights WHERE bookedFlightId = " + id;
		//String query = "UPDATE bookedFlights SET cancelled = 1 WHERE bookedFlightId = " + id;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}