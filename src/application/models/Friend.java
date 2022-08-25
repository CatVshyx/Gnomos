package application.models;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class Friend extends AnchorPane{
	@FXML
	private Label labelUsername;
	@FXML 
	private ImageView image;
	private FXMLLoader loader;

	public Friend(String username, BufferedImage im ) {
		loader = new FXMLLoader(getClass().getResource("/resources/fxml/friend.fxml"));
		loader.setController(this);
		loader.setRoot(this);

		try {
			loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		labelUsername.setText(username);
		if(im != null) {
			image.setImage(SwingFXUtils.toFXImage(im, null));
		}
	}
	public String getText() {
		return labelUsername.getText();
	}

	
}