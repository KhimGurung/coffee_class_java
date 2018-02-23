package FeePayment;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import org.controlsfx.control.textfield.TextFields;

import com.itextpdf.text.DocumentException;

import dbconnection.ConnectionManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
/*import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;*/
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class FeePaymentController {
	private Integer balance,year;
	private String sql;

    @FXML private Label hidden_id,room_number,status;

    @FXML private ComboBox<String> select_room_category;

    @FXML private TextField std_name,total_fee,total_paid,due_fee,pay_amount,search_by_name,search_by_invoice;

    @FXML private Pane btn_grp;

    @FXML private Button save_room, update_room, delete_room, add_new_room;

    @FXML private Pane heading;
    
    @FXML private TextArea room_desc;
    
    @FXML private TableView<Fee> student_trans_list;
   	@FXML private TableColumn<Fee, Integer> trans_id;
   	@FXML private TableColumn<Fee, String> name;
   	@FXML private TableColumn<Fee, String> date;
   	@FXML private TableColumn<Fee, Integer> amount;
   	@FXML private TableColumn<Fee, String> invoice;
   	
   	public ArrayList<String> oldrecord = new ArrayList<String>();
   	public ArrayList<String> newrecord = new ArrayList<String>();
   	public ArrayList<String> studentlist = new ArrayList<String>();
   	
    public ObservableList<Fee> studenttranslist;
    
    public PreparedStatement statement,statement1;

    public void initialize(){
    	
    	year=Calendar.getInstance().get(Calendar.YEAR);
		sql="SELECT transaction.trans_id, student.std_name, transaction.date, transaction.debit, transaction.invoice_no "+
				"FROM    student   "+
				"LEFT JOIN transaction "+
				"ON student.std_id = transaction.std_id WHERE transaction.date LIKE '"+year+"%' ORDER BY transaction.trans_id DESC";
		RefreshTable(sql);
		fetchstudent();
    	fetchstudentinfo();
    	generateinovice();
    }
    private void fetchstudent(){
    	try{
			Connection con = ConnectionManager.getConnection();
			PreparedStatement statement=con.prepareStatement("SELECT * FROM student");
			ResultSet result=statement.executeQuery();
			while(result.next()){
				studentlist.add(result.getInt(1)+" -- "+result.getString(2));
			}
			}catch(SQLException ex){
				System.err.println("Error"+ex);
			}
    	TextFields.bindAutoCompletion(std_name, studentlist);
    }
    private void fetchstudentinfo(){
   	 std_name.textProperty().addListener(new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
					if(!std_name.getText().isEmpty()){
					String number=std_name.getText().replaceAll("[^0-9]", "");
					if(!number.isEmpty()){
					Integer stdid= Integer.parseInt(number);
						try{
							Connection con = ConnectionManager.getConnection();
							String sql="SELECT student.std_id, student.fee,SUM(transaction.debit) "+
										"FROM    student   "+
										"LEFT JOIN transaction "+
										"ON student.std_id = transaction.std_id "+
										"WHERE student.std_id = "+stdid+
										" GROUP BY student.std_id;";
							PreparedStatement statement=con.prepareStatement(sql);
							ResultSet result=statement.executeQuery();
								while(result.next()){
									Integer hiddenid=result.getInt(1);
									Integer totalfee=result.getInt(2);
									Integer totalpaid=result.getInt(3);
									Integer deufee=totalfee-totalpaid;
									hidden_id.setText(hiddenid.toString());
									total_fee.setText(totalfee.toString());
									total_paid.setText(totalpaid.toString());
									due_fee.setText(deufee.toString());
									if(deufee == 0){
										pay_amount.setEditable(false);
									}
									else{
										pay_amount.setEditable(true);
									}
								}
							}catch(SQLException ex){
								System.err.println("Error"+ex);
							}
					}
				}
					else{
						clearcontrol();
					}
			}
	
   	    });
    }
    private void clearcontrol(){
    	hidden_id.setText("----");
		total_fee.clear();
		total_fee.setPromptText("0");
		total_paid.clear();
		total_paid.setPromptText("0");
		due_fee.clear();
		due_fee.setPromptText("0");
		pay_amount.clear();
		pay_amount.setPromptText("0");
    }
	@FXML
	private void validate_number(KeyEvent event) {
		 char input=event.getCharacter().charAt(0);
	    	if(!(Character.isDigit(input) || Character.isSpaceChar(input) || Character.toString(input).equals(""))){
	    		/*Toolkit.getDefaultToolkit().beep();*/
	    		event.consume();
	    	}
	    }
	@FXML
	void check_pay(KeyEvent event) {
		if((Integer.parseInt(pay_amount.getText())) > (Integer.parseInt(due_fee.getText()))){
    		Toolkit.getDefaultToolkit().beep();
    		event.consume();
    		pay_amount.clear();
    		pay_amount.setPromptText("0");
    	}
	   }
	@FXML
    void save_student_transaction(ActionEvent event) throws SQLException, IOException, DocumentException {
		UUID uniqueid=UUID.randomUUID();
	    String invoid = uniqueid.toString();
	    String reqchar = invoid.substring(0, Math.min(invoid.length(), 6));
	    reqchar="INVO"+reqchar;
		if(EmptyFieldValidation()){
			Connection con = ConnectionManager.getConnection();
			String sql="INSERT INTO transaction(trans_id,std_id,shr_id,date,debit,credit,balance,invoice_no) VALUES(?,?,?,?,?,?,?,?)";
			String sqll="SELECT balance FROM transaction ORDER BY trans_id DESC LIMIT 1";
			Integer id=Integer.parseInt(hidden_id.getText());
			Integer debit=Integer.parseInt(pay_amount.getText());
			PreparedStatement statements=con.prepareStatement(sqll);
			ResultSet result=statements.executeQuery();
			while(result.next()){
				 balance = result.getInt(1);
			}
			
		try{
			statement = con.prepareStatement(sql);
			statement.setDouble(1, 0);
			statement.setInt(2, id);
			statement.setInt(3, 0);
			statement.setString(4, LocalDate.now().toString());
			statement.setInt(5, debit);
			statement.setInt(6, 0);
			statement.setInt(7, balance+debit);
			statement.setString(8, reqchar.toUpperCase());
			
			int i=statement.executeUpdate();
			if(i==1){
				year=Calendar.getInstance().get(Calendar.YEAR);
				sql="SELECT transaction.trans_id, student.std_name, transaction.date, transaction.debit, transaction.invoice_no "+
						"FROM    student   "+
						"LEFT JOIN transaction "+
						"ON student.std_id = transaction.std_id WHERE transaction.date LIKE '"+year+"%' ORDER BY transaction.trans_id DESC";
				RefreshTable(sql);
				status.setText("Record Inserted!!!");
				std_name.clear();
				std_name.setPromptText("Enter student name or ID");
				clearcontrol();
				
				/*Stage primarystage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				Pane root = loader.load(getClass().getResource("/FeePayment/InvoiceWindow.fxml").openStream());
				InvoiceController invoicecontroller = (InvoiceController)loader.getController();
				invoicecontroller.setData(id,debit,reqchar);
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("Invoice.css").toExternalForm());
				primarystage.setScene(scene);
				primarystage.show();
				
				scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				    public void handle(MouseEvent mouseEvent) {
				    	AudioClip sound = new AudioClip(new File("click.aif").toURI().toString());
				    	sound.play();
				    }
				});*/
				InvoiceController invoicecontroller = new InvoiceController();
				invoicecontroller.setData(id,debit,reqchar);
				invoicecontroller.invoice();
				
				
				
			}
		}
		catch(SQLException ex){
				System.err.println("Error"+ex);
		}
		finally{
				statement.close();
				statements.close();
			}
	 }
    }
	private boolean EmptyFieldValidation(){
		if(std_name.getText().equals("") || pay_amount.getText().equals("")){
			if(std_name.getText().equals("")){
				std_name.setPromptText("Student name is mandatory.");
			}
			if(pay_amount.getText().equals("")){
				pay_amount.setPromptText("Enter paying amount.");
			}
				return false;
		}else{
		return true;
		}
	}
	void RefreshTable(String sql){
		try{
			Connection con = ConnectionManager.getConnection();
			studenttranslist=FXCollections.observableArrayList();
			PreparedStatement statement=con.prepareStatement(sql);
			ResultSet result=statement.executeQuery();
			while(result.next()){
				studenttranslist.add(new Fee(result.getInt(1),result.getString(2),result.getString(3),result.getInt(4),result.getString(5)));
			}
			}catch(SQLException ex){
				System.err.println("Error"+ex);
			}

			trans_id.setCellValueFactory(new PropertyValueFactory<Fee,Integer>("trans_id"));
			name.setCellValueFactory(new PropertyValueFactory<Fee,String>("name"));
			date.setCellValueFactory(new PropertyValueFactory<Fee,String>("date"));
			amount.setCellValueFactory(new PropertyValueFactory<Fee,Integer>("amount"));
			invoice.setCellValueFactory(new PropertyValueFactory<Fee,String>("invoice"));
			student_trans_list.setItems(studenttranslist);
		}
	
	private void generateinovice() {
		student_trans_list.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				if(event.getClickCount() == 2){ 
					Fee sc=student_trans_list.getItems().get(student_trans_list.getSelectionModel().getSelectedIndex());
					File myFile = new File("invoice/"+sc.getInvoice()+".pdf");
	                try {
						Desktop.getDesktop().open(myFile);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
	}
	
	 @FXML
	 void search_by_name(KeyEvent event) {
				String searchname=search_by_name.getText();
				sql="SELECT transaction.trans_id, student.std_name, transaction.date, transaction.debit, transaction.invoice_no "+
						"FROM    student   "+
						"LEFT JOIN transaction "+
						"ON student.std_id = transaction.std_id WHERE transaction.date LIKE '"+year+"%' AND student.std_name LIKE '%"+searchname+"%' ORDER BY transaction.trans_id DESC";
				RefreshTable(sql);
	 }
	 
	 @FXML
	 void search_by_invoice(KeyEvent event) {
		 String searchinvoice=search_by_invoice.getText();
			sql="SELECT transaction.trans_id, student.std_name, transaction.date, transaction.debit, transaction.invoice_no "+
					"FROM    student   "+
					"LEFT JOIN transaction "+
					"ON student.std_id = transaction.std_id WHERE transaction.date LIKE '"+year+"%' AND transaction.invoice_no LIKE '%"+searchinvoice+"%' ORDER BY transaction.trans_id DESC";
			RefreshTable(sql);
	 }
}
