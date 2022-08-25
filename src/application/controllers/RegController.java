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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;

public class RegController { 
    @FXML
    private Button rButtonOk;

    @FXML
    private TextField rEmailField;

    @FXML
    private TextField rLoginField;

    @FXML
    private PasswordField rPasswordField;

    @FXML
    private Button buttonSignIn;
    
    @FXML
    private ToggleGroup gender;
    
    @FXML
    private RadioButton rbMan;

    @FXML
    private RadioButton rbWoman;
    
    @FXML
    void  initialize(){
    	buttonSignIn.setOnAction( (e) ->{
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(getClass().getResource("/resources/fxml/mainScene.fxml"));
    		try {
				loader.load();
				Parent root = loader.getRoot();
				Main.getStage().setScene(new Scene(root,Main.WIDTH,Main.HEIGHT));
    		} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	});
    	rButtonOk.setOnAction((e) -> {
    		String login = rLoginField.getText();
    		String email = rEmailField.getText();
    		String password = rPasswordField.getText();
    		String sex = gender.getSelectedToggle().toString();
    		sex  = sex.substring(sex.indexOf("'")+1, sex.length()-1);
    		
    		if(login.length() <=3 ||email.length() <=10  || password.length() <= 8  ) {
    			new Alert(AlertType.INFORMATION,"Not Valid info").show();
    			return;
    		}
    		boolean a = DBConnector.registerUser(login, password, email, sex);
    		
    		if(!a) {
    			new Alert(AlertType.INFORMATION,"Such user is already registered").show();
    			return;
    		}
    		
    		new Alert(AlertType.INFORMATION,"you are succesefully registered").show();
			
			
			FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(getClass().getResource("/resources/fxml/mainScene.fxml"));
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