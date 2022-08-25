package application.controllers;

import application.DBConnector;
import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import application.models.User;
import application.models.Friend;

public class FriendController {

    @FXML
    private Button buttonAdd;

    @FXML
    private Label labelTotalFriends;

    @FXML
    private TextField textNickname;
    
    @FXML 
    private GridPane friendsGridPane; 
    
    private void updatePage() {
    	DBConnector.updateFriends();
    	User[] myFriends = Main.getUser().getFriends(); 
    	if(myFriends == null) return;
    	
    	friendsGridPane.getChildren().clear();
    	labelTotalFriends.setText(Integer.toString(myFriends.length));
    	
    	int column = 0;
    	int row = 1;
    	for (User u : myFriends) {
    		Friend m = new Friend(u.getUsername(),u.getImage());
    		m.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
    			String user = m.getText();
    			((Stage) friendsGridPane.getScene().getWindow()).close();
    			HomeController homeController = DBConnector.getHomeController();
    			homeController.setNews(homeController.getNews(u.getId()));
    			
    		});
    		
    		friendsGridPane.add(m, column, row++);

    	}
    }
    
    @FXML
    void initialize() {
    	updatePage();
    	buttonAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
    		String nickname = textNickname.getText();
    		if(nickname.length() <= 1 || nickname == null) {
    			new Alert(AlertType.INFORMATION,"Too little for a normal nickname , dude. Are you kidding me?").show();
    			return;
    		}
    		if(DBConnector.isAvailable(nickname) || (Main.getUser().toStringFriends().contains(nickname)) ) {
    			new Alert(AlertType.INFORMATION,"Such user is not available").show();
    			return;
    		};
    		if (nickname.equals(Main.getUser().getUsername())) {
    			new Alert(AlertType.INFORMATION,"Man are you so selfish?").show();
    			return;
    		}
    		DBConnector.addFriend(nickname);
    		updatePage();
    	});
    }
}
