package TransactionReport;

import java.io.File;
import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class TransactionReport {
	public TransactionReport() throws IOException{
		Stage newcustomerstage =new Stage();
		Parent root=FXMLLoader.load(getClass().getResource("TransactionReportWindow.fxml"));
		Scene scene=new Scene(root);
		scene.getStylesheets().add(getClass().getResource("TransactionReport.css").toExternalForm());
		//primaryStage.initStyle(StageStyle.UNDECORATED);
		newcustomerstage.setTitle("New Student");
		newcustomerstage.setScene(scene);
		newcustomerstage.show();
		
		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
		    public void handle(MouseEvent mouseEvent) {
		    	AudioClip sound = new AudioClip(new File("click.aif").toURI().toString());
		    	sound.play();
		    }
		});
	}
	
}
