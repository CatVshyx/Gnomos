package application;
	



import application.models.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import javafx.scene.Parent;
import javafx.scene.Scene;



public class Main extends Application {
	private static Stage stage;
	private static int UID = 1;
	public final static int  WIDTH = 800;
	public final static int HEIGHT = 600;
	private static User user;
	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/mainScene.fxml"));
			Scene s = new Scene(root,WIDTH,HEIGHT);
			primaryStage.setScene(s);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {

		DBConnector.connectDB();
		launch(args);

	}

	
	public static int getUID() {
		return UID;
	}

	public static void setUID(int uID) {
		UID = uID;
	}

	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		Main.user = user;
	}
	public static Stage getStage() {
		return stage;
	}


}
