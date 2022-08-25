package application.models;
import java.awt.image.BufferedImage;


public class User {
	private int id;
	private String username;
	private String email;
	private String sex;
	private BufferedImage image;
	private User[] friends;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setFriends(User[] users) {
		friends = users;
	}
	public User[]  getFriends() {
		return friends;
	}
	public String toStringFriends() {
		String  friendo = "";
		if(friends == null) return "";
		
		for (User friend : friends) {
			friendo += friend.getId()+",";
		}
		return friendo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
