package MainWindow;

import java.io.IOException;

import FeePayment.FeePayment;
import NewStudent.NewStudent;
import RevenueTaken.RevenueTaken;
import TransactionReport.TransactionReport;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class MainWindowController {

    @FXML
    private TabPane menutab;

    @FXML
    private Tab home;

    @FXML
    private Pane alert;

    @FXML
    private Pane resmanage;

    @FXML
    private ImageView new_student;

    @FXML
    private Label newreslbl;
    
    @FXML
    private AnchorPane toppane;

    @FXML
    void openTransactionReportWindow(MouseEvent event) throws IOException {
    	@SuppressWarnings("unused")
    	TransactionReport newres=new TransactionReport();
    }
    @FXML
    void openNewStudentWindow(MouseEvent event) throws IOException {
    	@SuppressWarnings("unused")
    	NewStudent newstd=new NewStudent();
    }
    @FXML
    void openRevenueTakenWindow(MouseEvent event) throws IOException {
    	@SuppressWarnings("unused")
    	RevenueTaken revenue=new RevenueTaken();
    }
    @FXML
    void openFeePaymentWindow(MouseEvent event) throws IOException {
    	@SuppressWarnings("unused")
		FeePayment payment=new FeePayment();
    }

}
