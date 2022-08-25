package application.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import application.DBConnector;
import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class PostCreatorController {
    @FXML
    private Button buttonShare;

    @FXML
    private TextArea textField;

    @FXML
    private TextField textTitle;
    
    @FXML
    void initialize() {
    	buttonShare.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
    		
			if(textField.getText().length() < 8 || textTitle.getText().length()  < 6 ) {
				new Alert(AlertType.INFORMATION,"please, write a normal post...").show();
				return;
			}
			HomeController.isUpdateable = true;
			this.sendPosts(addTags());
		});
    	
    }
    private String addTags() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("<post>");
    	sb.append("<title>"+textTitle.getText()+"</title>");
    	sb.append("<content>"+textField.getText()+"</content>");
    	sb.append("</post>");
    	return sb.toString();
    }
    private void sendPosts( String newPost ) {
		 String currentContent = DBConnector.getPosts2(Main.getUID());
		 
		 currentContent = (currentContent == null) ? newPost : currentContent+newPost;
		
		 File f = new File("send.txt");
		 byte[] arr = currentContent.getBytes();
		 try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(arr);
			fos.close();
			DBConnector.updatePosts(f, Main.getUID());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	 }
}
