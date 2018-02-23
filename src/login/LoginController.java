package login;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dbconnection.ConnectionManager;
import MainWindow.MainWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField uname;

    @FXML
    private PasswordField password;

    @FXML
    private Button login;

    @FXML
    private ImageView loginclose;

    @FXML
    private Label statuslbl;
    
    private Connection con;

    @FXML
    void closeLogin(MouseEvent event) {
    	Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void loginUser(ActionEvent event)throws Exception {
    	String dec_pass = null;
		String username=uname.getText();
		String pass=password.getText();
		if(username.equals("") || pass.equals("")){
			statuslbl.setText("Enter username and password !!!");
		}
		else{
		try{
			MessageDigest md=MessageDigest.getInstance("MD5");
			md.update(pass.getBytes(),0,pass.length());
			dec_pass=new BigInteger(1,md.digest()).toString(16);
		}catch(NoSuchAlgorithmException e){
			statuslbl.setText(e.toString());
		}

		con = ConnectionManager.getConnection();
		PreparedStatement statement=con.prepareStatement("SELECT * FROM user WHERE username='"+username+"' AND password='"+dec_pass+"'");
		ResultSet result=statement.executeQuery();
		if(result.next()){
		do{
			if(result.getString(3).equals(username) && result.getString(4).equals(dec_pass)){
				Node  source = (Node)  event.getSource(); 
		        Stage stage  = (Stage) source.getScene().getWindow();
		        stage.close();
				@SuppressWarnings("unused")
				MainWindow mw=new MainWindow();
			 }
		 }while(result.next());
		}else{
				statuslbl.setText("Invalid username or password !!!");
			}
		}
    }

}
