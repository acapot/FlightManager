package flightManager;

public class Airport {
	private int id;
	private String airportName;
	private City city;

	public String getAirportName() 
	{
		return airportName;
	}

	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Airport(String airportName, City city){
		this.airportName = airportName;
		this.city = city;
	}
	
	public Airport(int id, String airportName, City city){
		this.id = id;
		this.airportName = airportName;
		this.city = city;
	}
	
	@Override
	public String toString(){
		return this.airportName;
	}
}
