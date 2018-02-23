package FeePayment;

import java.io.File;
import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class FeePayment {
	public FeePayment() throws IOException{
		Stage feepayment=new Stage();
		Parent root=FXMLLoader.load(getClass().getResource("FeePaymentWindow.fxml"));
		Scene scene=new Scene(root);
		scene.getStylesheets().add(getClass().getResource("FeePayment.css").toExternalForm());
		//primaryStage.initStyle(StageStyle.UNDECORATED);
		feepayment.setTitle("Fee Payment Management");
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
