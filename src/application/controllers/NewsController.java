package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class NewsController {

    @FXML
    private TextArea textInfo;

    @FXML
    private Label title;
    
    @FXML
    private Label time;
    public void setData(String title, String text, String color) {
    	this.title.setText(title);
    	textInfo.setText(text);
    	textInfo.setEditable(false);
    	this.title.setStyle("-fx-background-color: " + color+ ";");
    }
}
