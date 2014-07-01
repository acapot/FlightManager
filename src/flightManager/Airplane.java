package flightManager;

public class Airplane {
	private int id;
	private String brand;
	private String model;
	private int fCapacity;
	private int sCapacity;
	private int tCapacity;
	private int velocity;
	private int kilometerCost;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getfCapacity() {
		return fCapacity;
	}
	public void setfCapacity(int fCapacity) {
		this.fCapacity = fCapacity;
	}
	public int getsCapacity() {
		return sCapacity;
	}
	public void setsCapacity(int sCapacity) {
		this.sCapacity = sCapacity;
	}
	public int gettCapacity() {
		return tCapacity;
	}
	public void settCapacity(int tCapacity) {
		this.tCapacity = tCapacity;
	}
	public int getVelocity() {
		return velocity;
	}
	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}
	
	@Override
	public String toString(){
		return brand + " " + model;
	}
	
	public int getKilometerCost() {
		return kilometerCost;
	}
	public void setKilometerCost(int kilometerCost) {
		this.kilometerCost = kilometerCost;
	}
	
	public Airplane(int id, String brand, String model, int fCapacity, int sCapacity, int tCapacity, int velocity, int kilometerCost){
		this.id = id;
		this.brand = brand;
		this.model = model;
		this.fCapacity = fCapacity;
		this.sCapacity = sCapacity;
		this.tCapacity = tCapacity;
		this.velocity = velocity;
		this.kilometerCost = kilometerCost;
	}
	
	public Airplane(String brand, String model, int fCapacity, int sCapacity, int tCapacity, int velocity, int kilometerCost){
		this.brand = brand;
		this.model = model;
		this.fCapacity = fCapacity;
		this.sCapacity = sCapacity;
		this.tCapacity = tCapacity;
		this.velocity = velocity;
		this.kilometerCost = kilometerCost;
	}

}

