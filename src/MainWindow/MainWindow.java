package MainWindow;

import java.io.File;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class MainWindow {
	public MainWindow() throws Exception{
		Stage primaryStage =new Stage();
		Parent root=FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
		Scene scene=new Scene(root);
		scene.getStylesheets().add(getClass().getResource("MainWindow.css").toExternalForm());
		//primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setTitle("Hotel Tara ");
		primaryStage.setMaximized(true);
		primaryStage.setScene(scene);
		primaryStage.show();
		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
		    public void handle(MouseEvent mouseEvent) {
		    	AudioClip sound = new AudioClip(new File("click.aif").toURI().toString());
		    	sound.play();
		    }
		});
	}
}
