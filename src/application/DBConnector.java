package application;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import javax.imageio.ImageIO;

import application.controllers.HomeController;
import application.models.User;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public  class DBConnector {
	private static String url;
	private static String username;
	private static String pass;
	private static HomeController hm;
	
	public static void connectDB() {
		try {

			FileReader reader = new FileReader("./src/application/config.properties");

			Properties props = new Properties();
			props.load(reader);
			url = "jdbc:mysql://" + props.getProperty("url") + props.getProperty("database");
			username = props.getProperty("username");
			pass = props.getProperty("password");
			reader.close();
			if(url == null || username == null || pass == null) {
				return;
			}
			
			
			
			Connection connect = getConnection();
			PreparedStatement ps = connect.prepareStatement("CREATE TABLE IF NOT EXISTS `appusers`"
					+ " (`id` INT NOT NULL AUTO_INCREMENT,"
					+ "`username` VARCHAR(80) NOT NULL,"
					+ "`password` VARCHAR(80) NOT NULL,"
					+ "`email` VARCHAR(90) NOT NULL,"
					+ "`sex` VARCHAR(10) NULL,"
					+ "`img` BLOB NULL,"
					+ "`friends` VARCHAR(1000) NULL,"
					+ "PRIMARY KEY (`id`));");
			ps.execute();

			ps = connect.prepareStatement("CREATE TABLE IF NOT EXISTS `posts` (`id` INT NOT NULL,`post` BLOB NULL,PRIMARY KEY (`id`));");
			ps.execute();
			connect.close();
			registerUser("dmitro","12345","dmitroshapo@gmail.com","man");
			registerUser("IGOR","girsof","igoro@gmail.com","man");
			registerUser("Gaso","77771111","gasos@gmail.com","man");
			registerUser("igoros","girsof","igogo@gmail.com","man");
			
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static Connection getConnection() {
		 Connection connection;
		try {
			connection = DriverManager.getConnection(url, username, pass);
			return connection;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	     
	}
	public static void setHomeController(HomeController controller) {
		hm = controller;
	}
	public static HomeController getHomeController() {
		return hm;
	}
	public static boolean checkUser(String login, String password){
		try {
			Connection connect = getConnection();
			PreparedStatement ps = connect.prepareStatement("SELECT * FROM appusers WHERE username = ?");
			ps.setString(1, login);
			ResultSet s = ps.executeQuery();
			while(s.next()) {
				if (s.getString(3).equals(password)) {
					Main.setUID(s.getInt(1));
					System.out.println("you are welcome");
					return true;
				}
			}
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public static void getSexInfo() {
		//множественные процедуры 
		try {
			Connection connect = getConnection();
			CallableStatement cbs = connect.prepareCall("call getSexInfo()");
			boolean has = cbs.execute();
			while (has) {
				ResultSet rs = cbs.getResultSet();
				while(rs.next()) {
					System.out.println(rs.getInt(1));
				}
				has = cbs.getMoreResults();
			}
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static int getTotalUsers() {
		// working with procedurs
		try {
			Connection connect = getConnection();
			CallableStatement cbStatement = connect.prepareCall("call usersCount(?)");
			cbStatement.registerOutParameter(1, Types.INTEGER);
			cbStatement.execute();
			
			int v = cbStatement.getInt(1);
			connect.close();
			return v;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
	}
	public static void updateFriends() {
		int id = Main.getUID();
		Connection connect = getConnection();
		try {
			PreparedStatement ps = connect.prepareStatement("SELECT * FROM appusers WHERE id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			String str = null;
			while(rs.next()) {
				str = rs.getString("friends");
				break;
			}
			if (str == null) return;
			Main.getUser().setFriends( parseFriends(str));
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static User getFriend(User u, int id) {
		Connection connect = getConnection();
		try {
			PreparedStatement ps = connect.prepareStatement("SELECT id,username, img FROM appusers WHERE id = ?");
			ps.setInt(1,id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				u.setUsername(rs.getString("username"));
				u.setId(rs.getInt("id"));
				Blob blob = rs.getBlob("img");
				if(blob == null) {
					continue;
				}
				byte[] byteArr = blob.getBytes(1, (int) blob.length());
				ByteArrayInputStream bis = new ByteArrayInputStream(byteArr);
			    BufferedImage bImage2 = ImageIO.read(bis);
			    u.setImage(bImage2);
			    bis.close();
			    return u;
			}
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static User getUser() {
		try {
			Connection connect = getConnection();
			PreparedStatement ps = connect.prepareStatement("SELECT * FROM appusers WHERE id = ?");
			ps.setInt(1, Main.getUID());
			ResultSet rs = ps.executeQuery();
			User user = new User();
			while (rs.next()) {
				user.setUsername(rs.getString(2));
				user.setEmail(rs.getString(4));
				user.setSex(rs.getString(5));
				Blob blob = rs.getBlob("img");
				if(blob == null) {
					continue;
				}
				byte[] byteArr = blob.getBytes(1, (int) blob.length());
				ByteArrayInputStream bis = new ByteArrayInputStream(byteArr);
			    BufferedImage bImage2 = ImageIO.read(bis);
			    user.setImage(bImage2);
			    bis.close(); // perhaps
			}
			connect.close();
			return user;
		} catch (SQLException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	private static User[] parseFriends(String myFriends) {
		String[] temp = myFriends.split(",");
		User[] userFriends = new User[temp.length];
		for(int i = 0; i < temp.length; i++) {
			User u = new User();
			userFriends[i] = getFriend(u,Integer.parseInt(temp[i]));
		}
		return userFriends;
	}
	public static String getPosts2(int id) {
		try {
			Connection connect = getConnection();
			PreparedStatement ps = connect.prepareStatement("SELECT post FROM posts WHERE id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			StringBuilder sb = new StringBuilder();
			while(rs.next()) {
				Blob b = rs.getBlob(1);
				if(b == null) return null;
				InputStream in = b.getBinaryStream();
				int symbol;
				while( (symbol = in.read()) != -1 ) {
					sb.append((char)symbol);
				}
				in.close();
			}
			connect.close();
			return sb.toString();
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void updatePosts(File f, int id){
		
		try {
			Connection connect = getConnection();
			PreparedStatement ps = connect.prepareStatement("UPDATE posts SET post = ? WHERE id = ?");
			
			ps.setBlob(1, new FileInputStream(f));
			ps.setInt(2, Main.getUID());
			ps.execute();
			connect.close();
		} catch (SQLException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean isAvailable(String newLogin) {
		try {
			Connection connect = getConnection();
			PreparedStatement ps = connect.prepareStatement("SELECT username FROM appusers WHERE username = ?");
			ps.setString(1, newLogin);
			ResultSet s = ps.executeQuery();
			if(s.next()) {
				return false;
			}
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
		
	}
	public static boolean registerUser(String login, String password, String email, String sex) {
		// ADD  A NEW USER INTO POSTSTABLE
		try {			
			if(!isAvailable(login)) {
				return false;
			}
			Connection connect = getConnection();
			PreparedStatement ps = connect.prepareStatement("INSERT INTO appusers(username,password,email,sex) VALUES(?,?,?,?)");
			
			ps.setString(1, login);
			ps.setString(2, password);
			ps.setString(3, email);
			ps.setString(4, sex);
			ps.execute();
			ps = connect.prepareStatement("SELECT id FROM appusers WHERE username = ? ");
			ps.setString(1, login);
			ResultSet rs = ps.executeQuery();
			int numb = 0;
			while(rs.next()) {
				numb = rs.getInt("id");
				
				
				
			}
			ps = connect.prepareStatement("INSERT INTO posts(ID) VALUES(?)");
			ps.setInt(1, numb);
			ps.execute();
			

			connect.close();
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public static void updateUser(String username,String email,String sex) {
		
		try {
			if(!isAvailable(username)) {
				return;
			}
			Connection connect = getConnection();
			PreparedStatement s = connect.prepareStatement("UPDATE appusers SET username = ?, email = ?, sex = ? WHERE id = ?");
			s.setString(1, username);
			s.setString(2,  email);
			s.setString(3, sex);
			s.setInt(4,Main.getUID());
			s.execute();
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	public static boolean addFriend(String username) {
		// later i can rebuild this function like sending a request to a user - does he want to make friends or not , 
		// but actially it needs basic understanding of sending-getting such messages
		// over the internet and receiving them right into the app 
		
		try {
			Connection connect = getConnection();
			PreparedStatement ps = connect.prepareStatement("SELECT id FROM appusers WHERE username = ?"  );
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			int id = 0;
			while(rs.next()) {
				id = rs.getInt("id");
			}
			String myFriends = Main.getUser().toStringFriends();
			myFriends += id + ",";
			System.out.println(myFriends);
			ps = connect.prepareStatement("UPDATE appusers SET friends = ? WHERE id =  ?");
			ps.setString(1,myFriends);
			ps.setInt(2, Main.getUID());
			ps.execute();
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public static void updateImage(String path) {
		try {
			//change it a bit bcs there is some cringe here
			File f = new File(path);
			if(f.length()/1000 > 64) {
				return;
			}
			Connection connect = getConnection();
			PreparedStatement s = connect.prepareStatement("UPDATE appusers SET img = ? WHERE id = ?");
			InputStream in = new FileInputStream(f);
			s.setBlob(1, in);
			s.setInt(2, Main.getUID());
			s.executeUpdate();
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
