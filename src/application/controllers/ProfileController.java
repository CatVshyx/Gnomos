package application.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
import java.awt.image.BufferedImage;
import java.io.File;

import application.DBConnector;
import application.models.User;

public class ProfileController {

    @FXML
    private AnchorPane anchorImage;

    @FXML
    private Button buttonSave;

    @FXML
    private TextField emailField;

    @FXML
    private ToggleGroup gender;

    @FXML
    private ImageView imageView;

    @FXML
    private RadioButton rbMan;

    @FXML
    private RadioButton rbWoman;

    @FXML
    private TextField unField;
    
   
    @FXML
    void initialize() {
    	this.setUser();
    	buttonSave.setOnMouseClicked( (e) -> {
    		DBConnector.updateUser(unField.getText(), emailField.getText(), ( gender.getSelectedToggle().equals(rbMan) ? "man" : "woman"));
    	});
    	anchorImage.setOnMouseClicked((e) -> {
    		File f = new FileChooser().showOpenDialog(new Stage());
    		if (f == null)  return; 

			String type = f.getAbsolutePath();
			type = type.substring(type.indexOf(".")+1,type.length());

			if (!type.equals("png") && !type.equals("jpg")) {
				new Alert(AlertType.INFORMATION,"ONLY JPG AND PNG").show();
	    		return;
			}
			
			DBConnector.updateImage(f.getAbsolutePath());
			User u = DBConnector.getUser();
			if(u.getImage() == null) {
				new Alert(AlertType.INFORMATION,"This is image is too big, take another one").show();
	    		return;
			}
			imageView.setImage(SwingFXUtils.toFXImage(u.getImage(), null));

    	});	
    	
    }

    public void setUser() {
    	User u = DBConnector.getUser();
    	
    	unField.setText(u.getUsername());
    	emailField.setText(u.getEmail());
    	if(u.getSex() == null) {
    		gender.selectToggle(rbMan);
    	}
    	else {
    		gender.selectToggle( (u.getSex().equals("man")) ? rbMan : rbWoman );
    	}
    	BufferedImage i = u.getImage();
    	if (i == null) return;
    	
    	imageView.setImage(SwingFXUtils.toFXImage(i, null));
    }
}
