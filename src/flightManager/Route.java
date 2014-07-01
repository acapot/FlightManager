package flightManager;

public class Route {

	private int id;
	private Airport start;
	private Airport destination;
	private int distance;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Airport getStart() {
		return start;
	}

	public void setStart(Airport start) {
		this.start = start;
	}

	public Airport getDestination() {
		return destination;
	}

	public void setDestination(Airport destination) {
		this.destination = destination;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	@Override
	public String toString(){
		String string = this.start.getAirportName();
		string += " -> ";
		string += this.destination.getAirportName();
		return string;
	}
	
	public Route(int id, Airport start, Airport destination, int distance){
		this.id = id;
		this.start = start;
		this.destination = destination;
		this.distance = distance;
	}
	
	public Route(Airport start, Airport destination, int distance){
		this.start = start;
		this.destination = destination;
		this.distance = distance;
	}
}
