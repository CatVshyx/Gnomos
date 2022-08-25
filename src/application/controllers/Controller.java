package application.controllers;

import java.io.IOException;

import application.DBConnector;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;


public class Controller {
    @FXML
    private Button enterButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpButton;

    @FXML
    void initialize() {
    	enterButton.setOnAction((e) ->{
    		if(!DBConnector.checkUser(loginField.getText(), passwordField.getText())) {
    			Alert alert = new Alert(AlertType.INFORMATION,"Not such user");
    			alert.show();
    			return;
    		}
        	FXMLLoader loader = new FXMLLoader();
        	Main.setUser(DBConnector.getUser());
        	loader.setLocation(getClass().getResource("/resources/fxml/home.fxml"));
        	try {
    			loader.load();
    			Parent root = loader.getRoot();
    			Main.getStage().setScene(new Scene(root,Main.WIDTH,Main.HEIGHT));
        	} catch (IOException e1) {
    				// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
    		
    		
    	});
    	signUpButton.setOnAction( (e) ->{
    		
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(getClass().getResource("/resources/fxml/regScene.fxml"));
    		try {
				loader.load();
				Parent root = loader.getRoot();

				Main.getStage().setScene(new Scene(root,Main.WIDTH,Main.HEIGHT));
    		} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		
    	});
    }
}
