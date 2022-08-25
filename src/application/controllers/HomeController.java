package application.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.DBConnector;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import application.models.News;

public class HomeController {
	

    @FXML
    private Button buttonShare;

    @FXML
    private TextArea textField;

    @FXML
    private TextField textTitle;
	
     @FXML
     private BorderPane borderPane;
    
	 @FXML
	 private Button buttonCreate;
	
	 @FXML
	 private ImageView friendsBTN;

	 @FXML
	 private ImageView profileBTN;

	 @FXML
	 private ImageView settingsBTN;
	 
	 @FXML 
	 private Label labelnfo;
	 
	 @FXML
	 private ImageView logOutButton;
	 
	 @FXML
	 private GridPane gridPane;
	 
	 @FXML
	 private ScrollPane scrollPane;
	 private List<News> myNews;
	 boolean clicked = false;
	 public static boolean isUpdateable = false;
	 
	 private void updatePosts() {
		 
		 gridPane.getChildren().clear();
		 this.setNews(this.getNews(Main.getUID()));
	 }
	 public List<News> getNews(int id) {
		 myNews = new ArrayList<>();
		 String content = DBConnector.getPosts2(id);
		 if( content == null ) return null;
		 System.out.println(content);
		 ArrayList<String[]> myPosts =  parseContent(content);
		 for(String[] arr : myPosts) {
			 News  n = new News();
			 n.setTitle(arr[0]);
			 n.setText(arr[1]);
			 n.setColor("red");
			 myNews.add(n);
		 }
		 return myNews;
	 }
	 
	 

	 public void setNews(List<News> myNews) {
		 // This function sets the news onto gridPane.
		 int column = 0;
		 int row = 1;
		 
		 if(myNews == null) {
			 Label l = new Label("You havent got any posts yet. Just create a new post ");
			 borderPane.setCenter(l);
			 return;
		 }
		 gridPane.getChildren().clear();
		 for(News n : myNews) {
			 FXMLLoader loader = new FXMLLoader();
			 loader.setLocation(getClass().getResource("/resources/fxml/news.fxml"));
			 try {
				AnchorPane acnhorPane = loader.load();
				NewsController nc = loader.getController();
				nc.setData(n.getTitle(), n.getText(), n.getColor());
				
				gridPane.add(acnhorPane, column, row++);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 }
	 }
	 private static ArrayList<String[]> parseContent(String content) {
		 // its a kind of a parser , finds tags and divide the information into them and 
		 // returns an array of posts with divided information e.g. title text , so it`s a lot easily to get them by their index in array aka [0] - title [1] -text and so on 
			ArrayList<String[]> al = new ArrayList<>();
			String[] arr = content.split("<post>");
			for(String str : arr) {
				if (str.length()  <= 1) continue;
				
				String[] post  = new String[2];
				post[0] = (str.substring(str.indexOf("<title>")+7, str.indexOf("</title>")));
				post[1] = (str.substring(str.indexOf("<content>")+9, str.indexOf("</content>")));
				al.add(post);
			}
			return al; 
		 }
	
	 
	 @FXML
	 public void initialize() {
		setNews(getNews(Main.getUID()));
		DBConnector.setHomeController(this);
		labelnfo.setText(labelnfo.getText()+DBConnector.getTotalUsers());
		logOutButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			Main.setUID(0);
			Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("/resources/fxml/mainScene.fxml"));
				Scene s = new Scene(root,800,600);
				Main.getStage().setScene(s);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			 
		 });
		 
		
		
		
		 profileBTN.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	         Stage stage = new Stage();
	         Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("/resources/fxml/userProfile.fxml"));
				Scene s = new Scene(root,400,600);
				stage.setScene(s);
				stage.setResizable(false);
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
			 
			 
	         event.consume();
	     });
		 
		 
		 buttonCreate.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			 try {
				if(!clicked) {
				Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/postCreator.fxml"));
				
				borderPane.setCenter(root);
				
				clicked = true;
				buttonCreate.setText("Read Posts");
				}
				else {
					if(isUpdateable) {
						this.updatePosts();
						isUpdateable = false;
					}
					buttonCreate.setText("Create post");
					borderPane.setCenter(scrollPane);
					clicked = false;
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 });
		 
		 settingsBTN.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	         System.out.println("Tile pressed ");
	         event.consume();
	     });
		 
		 
		 friendsBTN.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			 Stage stage = new Stage();
	         Parent root;
			try {
				root = FXMLLoader.load(getClass().getResource("/resources/fxml/friendList.fxml"));
				Scene s = new Scene(root,260,400);
				stage.setScene(s);
				stage.setResizable(false);
				stage.show();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	         event.consume();
	     });
	 }
}
