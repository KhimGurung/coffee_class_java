package RevenueTaken;

import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

import org.controlsfx.control.textfield.TextFields;

import dbconnection.ConnectionManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class RevenueTakenController {
	private Integer balance,share,year;
	private String sql;

    @FXML private Label hidden_id,room_number,status,revenue_taken,total_revenue,current_balance,your_share;

    @FXML private ComboBox<String> select_room_category;

    @FXML private TextField share_name,total_fee,total_paid,due_fee,enter_amount;

    @FXML private Pane btn_grp;

    @FXML private Button save_room, update_room, delete_room, add_new_room;

    @FXML private Pane heading;
    
    @FXML private TableView<Revenue> share_trans_list;
   	@FXML private TableColumn<Revenue, Integer> trans_id;
   	@FXML private TableColumn<Revenue, String> name;
   	@FXML private TableColumn<Revenue, String> date;
   	@FXML private TableColumn<Revenue, Integer> amount;
   	
   	public ArrayList<String> oldrecord = new ArrayList<String>();
   	public ArrayList<String> newrecord = new ArrayList<String>();
   	public ArrayList<String> shareholderlist = new ArrayList<String>();
   	
    public ObservableList<Revenue> shrtranslist;
    
    public PreparedStatement statement,statement1;

    public void initialize(){
    	year=Calendar.getInstance().get(Calendar.YEAR);
		sql="SELECT transaction.trans_id, shareholder.name, transaction.date, transaction.credit "+
				"FROM    shareholder   "+
				"LEFT JOIN transaction "+
				"ON shareholder.id = transaction.shr_id WHERE transaction.date LIKE '"+year+"%' ORDER BY transaction.trans_id DESC";
		RefreshTable(sql);
    	 fetchstudent();
    	 fetchstudentinfo();
    }
    private void fetchstudent(){
    	try{
			Connection con = ConnectionManager.getConnection();
			PreparedStatement statement=con.prepareStatement("SELECT * FROM shareholder");
			ResultSet result=statement.executeQuery();
			while(result.next()){
				shareholderlist.add(result.getInt(1)+" -- "+result.getString(2));
			}
			}catch(SQLException ex){
				System.err.println("Error"+ex);
			}
    	TextFields.bindAutoCompletion(share_name, shareholderlist);
    }
    private void fetchstudentinfo(){
   	 share_name.textProperty().addListener(new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
					if(!share_name.getText().isEmpty()){
					String number=share_name.getText().replaceAll("[^0-9]", "");
					if(!number.isEmpty()){
					Integer stdid= Integer.parseInt(number);
						try{
							Connection con = ConnectionManager.getConnection();
							String sql="SELECT shareholder.id, shareholder.percentage, SUM(transaction.credit)"+
										"FROM    shareholder   "+
										"LEFT JOIN transaction "+
										"ON shareholder.id = transaction.shr_id "+
										"WHERE shareholder.id = "+stdid+
										" GROUP BY shareholder.id;";

							PreparedStatement statement=con.prepareStatement(sql);
							ResultSet result=statement.executeQuery();
								while(result.next()){
									Integer hiddenid=result.getInt(1);
									share=result.getInt(2);
									Integer revenuetaken=result.getInt(3);
									
									hidden_id.setText(hiddenid.toString());
									revenue_taken.setText(revenuetaken.toString());
								}
							sql="SELECT SUM(debit) FROM transaction;";
							statement=con.prepareStatement(sql);
							result=statement.executeQuery();
								while(result.next()){
									Integer totalrevenue=result.getInt(1);
									share = share*totalrevenue;
									share = share/100;
									total_revenue.setText(totalrevenue.toString());
									your_share.setText(share.toString());
								}
							sql="SELECT balance FROM transaction ORDER BY trans_id DESC LIMIT 1;";
							statement=con.prepareStatement(sql);
							result=statement.executeQuery();
								while(result.next()){
									Integer currentbalance=result.getInt(1);
									current_balance.setText(currentbalance.toString());
									if(currentbalance <= 0){
										enter_amount.setEditable(false);
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
		total_revenue.setText("00.00");
		current_balance.setText("00.00");
		revenue_taken.setText("00.00");
		your_share.setText("00.00");
		enter_amount.clear();
		enter_amount.setPromptText("0");
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
		Integer share_to_be_taken=Integer.parseInt(your_share.getText())-Integer.parseInt(revenue_taken.getText());
		Integer previous_amount = Integer.parseInt(enter_amount.getText())/10;
		if((Integer.parseInt(enter_amount.getText())) > share_to_be_taken){
			enter_amount.setText(previous_amount.toString());
			Toolkit.getDefaultToolkit().beep();
		}
		enter_amount.positionCaret(enter_amount.getText().length());
	   }
	@FXML
    void save_shareholder_transaction(ActionEvent event) throws SQLException {
		if(EmptyFieldValidation()){
			Connection con = ConnectionManager.getConnection();
			String sql="INSERT INTO transaction(trans_id,std_id,shr_id,date,debit,credit,balance,invoice_no) VALUES(?,?,?,?,?,?,?,?)";
			String sqll="SELECT balance FROM transaction ORDER BY trans_id DESC LIMIT 1";
			Integer id=Integer.parseInt(hidden_id.getText());
			Integer credit=Integer.parseInt(enter_amount.getText());
			PreparedStatement statements=con.prepareStatement(sqll);
			ResultSet result=statements.executeQuery();
			while(result.next()){
				 balance = result.getInt(1);
			}
			
		try{
			statement = con.prepareStatement(sql);
			statement.setDouble(1, 0);
			statement.setInt(2,0);
			statement.setInt(3, id);
			statement.setString(4,LocalDate.now().toString());
			statement.setInt(5,0);
			statement.setInt(6,credit);
			statement.setInt(7,balance-credit);
			statement.setString(8,"");
			
			int i=statement.executeUpdate();
			if(i==1){
				year=Calendar.getInstance().get(Calendar.YEAR);
				sql="SELECT transaction.trans_id, shareholder.name, transaction.date, transaction.credit "+
						"FROM    shareholder   "+
						"LEFT JOIN transaction "+
						"ON shareholder.id = transaction.shr_id WHERE transaction.date LIKE '"+year+"%' ORDER BY transaction.trans_id DESC";
				RefreshTable(sql);
				status.setText("Record Inserted!!!");
				share_name.clear();
				share_name.setPromptText("Enter shareholder name or ID");
				clearcontrol();
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
		if(share_name.getText().equals("") || enter_amount.getText().equals("")){
			if(share_name.getText().equals("")){
				share_name.setPromptText("shareholder name is mandatory.");
			}
			if(enter_amount.getText().equals("")){
				enter_amount.setPromptText("Enter paying amount.");
			}
				return false;
		}else{
		return true;
		}
	}
	void RefreshTable(String sql){
		try{
			Connection con = ConnectionManager.getConnection();
			shrtranslist=FXCollections.observableArrayList();
			PreparedStatement statement=con.prepareStatement(sql);
			ResultSet result=statement.executeQuery();
			while(result.next()){
				shrtranslist.add(new Revenue(result.getInt(1),result.getString(2),result.getString(3),result.getInt(4)));
			}
			}catch(SQLException ex){
				System.err.println("Error"+ex);
			}

			trans_id.setCellValueFactory(new PropertyValueFactory<Revenue,Integer>("trans_id"));
			name.setCellValueFactory(new PropertyValueFactory<Revenue,String>("name"));
			date.setCellValueFactory(new PropertyValueFactory<Revenue,String>("date"));
			amount.setCellValueFactory(new PropertyValueFactory<Revenue,Integer>("amount"));
			share_trans_list.setItems(shrtranslist);
		}
}
