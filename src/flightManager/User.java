package flightManager;

public class User {
	private int id;
	private int userRank;
	private String userName;
	private String password;
	private String email;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserRank() {
		return userRank;
	}
	public void setUserRank(int userRank) {
		this.userRank = userRank;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String username) {
		this.userName = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public User(String userName, int userRank, String email){
		this.userName = userName;
		this.userRank = userRank;
		this.email = email;
	}
	public User(String userName, String password, int userRank, String email){
		this.userName = userName;
		this.password = password;
		this.userRank = userRank;
		this.email = email;
	}
	public User(int id, String userName, int userRank, String email){
		this.id = id;
		this.userName = userName;
		this.userRank = userRank;
		this.email = email;
	}
	public User(int id, String userName, String password, int userRank, String email){
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.userRank = userRank;
		this.email = email;
	}

	@Override
	public String toString(){
		return userName;
	}
}
