package RevenueTaken;

import java.io.File;
import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class RevenueTaken {
	public RevenueTaken() throws IOException{
		Stage feepayment=new Stage();
		Parent root=FXMLLoader.load(getClass().getResource("RevenueTakenWindow.fxml"));
		Scene scene=new Scene(root);
		scene.getStylesheets().add(getClass().getResource("RevenueTaken.css").toExternalForm());
		//primaryStage.initStyle(StageStyle.UNDECORATED);
		feepayment.setTitle("Revenue Taken Record");
		feepayment.setScene(scene);
		feepayment.show();
		
		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
		    public void handle(MouseEvent mouseEvent) {
		    	AudioClip sound = new AudioClip(new File("click.aif").toURI().toString());
		    	sound.play();
		    }
		});
	}
}
