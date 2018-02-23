package TransactionReport;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ResourceBundle;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import dbconnection.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class TransactionReportController extends PdfPageEventHelper implements Initializable {
	String sql;
	Integer year,moneyin,moneyout;
	@FXML
	public Label money_in;
	public Label money_out;
	public Label balancee;
	public Label trans_title;
	
	@FXML
	public TextField by_date,report_heading;
	
	@FXML
	public Button only_in, only_out, reset, arfan, sujan;
	
	@FXML
	public TextArea details;

	
	@FXML
	public Pane btn_grp;
	public Pane heading;

	
	public PreparedStatement statement;
	
	
	@FXML private TableView<Transaction> trans_list;
	@FXML private TableColumn<Transaction, Integer> trans_id;
	@FXML private TableColumn<Transaction, String> student_name;
	@FXML private TableColumn<Transaction, Integer> debit;
	@FXML private TableColumn<Transaction, Integer> credit;
	@FXML private TableColumn<Transaction, Integer> balance;
	@FXML private TableColumn<Transaction, String> date;
	
	public ObservableList<Transaction> translist;
	public void initialize(URL arg0, ResourceBundle arg1) {
		year=Calendar.getInstance().get(Calendar.YEAR);
		sql="SELECT transaction.trans_id,student.std_name,shareholder.name,transaction.debit,transaction.credit,transaction.balance,transaction.date FROM transaction LEFT JOIN student ON transaction.std_id=student.std_id LEFT JOIN shareholder ON transaction.shr_id=shareholder.id WHERE transaction.date LIKE '"+year+"%' ORDER BY transaction.trans_id ASC";
		RefreshTable(sql);
	}

	@FXML
    private void validate_name(KeyEvent event) {
    	char input=event.getCharacter().charAt(0);
    	if(!(Character.isAlphabetic(input) || Character.isSpaceChar(input) || Character.toString(input).equals(""))){
    		/*Toolkit.getDefaultToolkit().beep();*/
    		event.consume();
    	}
    }
	@FXML
	private void validate_number(KeyEvent event) {
		 char input=event.getCharacter().charAt(0);
	    	if(!(Character.isDigit(input) || Character.toString(input).equals("-"))){
	    		/*Toolkit.getDefaultToolkit().beep();*/
	    		event.consume();
	    	}
	    }	 

	void RefreshTable(String sql){
		moneyin=0;
		moneyout=0;
		try{
			Connection con = ConnectionManager.getConnection();
			translist=FXCollections.observableArrayList();
			PreparedStatement statement=con.prepareStatement(sql);
			ResultSet result=statement.executeQuery();
			while(result.next()){
				String name = (result.getString(2) == null) ? result.getString(3):result.getString(2);
				translist.add(new Transaction(result.getInt(1),name,result.getString(3),result.getInt(4),result.getInt(5),result.getInt(6),result.getString(7)));
				moneyin=moneyin+result.getInt(4);
				moneyout=moneyout+result.getInt(5);
			}
			sql="SELECT balance FROM transaction ORDER BY trans_id DESC LIMIT 1";
			statement = con.prepareStatement(sql);
			result = statement.executeQuery();
			while(result.next()){
				money_in.setText("Rs."+moneyin.toString());
				money_out.setText("Rs."+moneyout.toString());
				Integer balance=result.getInt(1);
				balancee.setText("Rs."+balance.toString());
			}
			}catch(SQLException ex){
				System.err.println("Error"+ex);
			}

			trans_id.setCellValueFactory(new PropertyValueFactory<Transaction,Integer>("trans_id"));
			student_name.setCellValueFactory(new PropertyValueFactory<Transaction,String>("std_name"));
			debit.setCellValueFactory(new PropertyValueFactory<Transaction,Integer>("debit"));
			credit.setCellValueFactory(new PropertyValueFactory<Transaction,Integer>("credit"));
			balance.setCellValueFactory(new PropertyValueFactory<Transaction,Integer>("balance"));
			date.setCellValueFactory(new PropertyValueFactory<Transaction,String>("date"));
			trans_list.setItems(translist);
	}


	
	@FXML
	void search_by_money_in(ActionEvent event) {
		getdate();
		sql="SELECT transaction.trans_id,student.std_name,shareholder.name,transaction.debit,transaction.credit,transaction.balance,transaction.date FROM transaction LEFT JOIN student ON transaction.std_id=student.std_id LEFT JOIN shareholder ON transaction.shr_id=shareholder.id WHERE transaction.date LIKE '"+year+"%' AND transaction.credit=0 ORDER BY transaction.trans_id ASC";
		RefreshTable(sql);
	}
	@FXML
	void search_by_money_out(ActionEvent event) {
		getdate();
		sql="SELECT transaction.trans_id,student.std_name,shareholder.name,transaction.debit,transaction.credit,transaction.balance,transaction.date FROM transaction LEFT JOIN student ON transaction.std_id=student.std_id LEFT JOIN shareholder ON transaction.shr_id=shareholder.id WHERE transaction.date LIKE '"+year+"%' AND transaction.debit=0 ORDER BY transaction.trans_id ASC";
		RefreshTable(sql);
	}
    @FXML
    void search_by_date(KeyEvent event) {
    	sql="SELECT transaction.trans_id,student.std_name,shareholder.name,transaction.debit,transaction.credit,transaction.balance,transaction.date FROM transaction LEFT JOIN student ON transaction.std_id=student.std_id LEFT JOIN shareholder ON transaction.shr_id=shareholder.id WHERE transaction.date LIKE '%"+by_date.getText()+"%' ORDER BY transaction.trans_id ASC";
		RefreshTable(sql);
    }
    @FXML
    void reset(ActionEvent event){
    	year=Calendar.getInstance().get(Calendar.YEAR);
		sql="SELECT transaction.trans_id,student.std_name,shareholder.name,transaction.debit,transaction.credit,transaction.balance,transaction.date FROM transaction LEFT JOIN student ON transaction.std_id=student.std_id LEFT JOIN shareholder ON transaction.shr_id=shareholder.id WHERE transaction.date LIKE '"+year+"%' ORDER BY transaction.trans_id ASC";
		RefreshTable(sql);
    }
    
    @FXML
    void search_by_sujan(ActionEvent event) {
    	getdate();
		sql="SELECT transaction.trans_id,student.std_name,shareholder.name,transaction.debit,transaction.credit,transaction.balance,transaction.date FROM transaction LEFT JOIN student ON transaction.std_id=student.std_id LEFT JOIN shareholder ON transaction.shr_id=shareholder.id WHERE transaction.date LIKE '"+year+"%' AND shareholder.id = 7 ORDER BY transaction.trans_id ASC";
		RefreshTable(sql);
    }
    @FXML
    void search_by_arfan(ActionEvent eventt) {
    	getdate();
		sql="SELECT transaction.trans_id,student.std_name,shareholder.name,transaction.debit,transaction.credit,transaction.balance,transaction.date FROM transaction LEFT JOIN student ON transaction.std_id=student.std_id LEFT JOIN shareholder ON transaction.shr_id=shareholder.id WHERE transaction.date LIKE '"+year+"%' AND shareholder.id = 8 ORDER BY transaction.trans_id ASC";
		RefreshTable(sql);
    }
    private void getdate(){
    	if(!(by_date.getText().equals(""))){
    		year= Integer.parseInt(by_date.getText());
    	}
    }
    @FXML
	void create_pdf(ActionEvent events) throws FileNotFoundException, DocumentException {
    	try{
    		
    		Connection con = ConnectionManager.getConnection();
			PreparedStatement statement=con.prepareStatement(sql);
			ResultSet result=statement.executeQuery();
			 Document document = new Document(PageSize.A4, 36, 36, 105, 36);
		        @SuppressWarnings("unused")
				PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Report.pdf"));
		        // add header and footer
		        //HeaderFooterPageEvent event = new HeaderFooterPageEvent();
		        //writer.setPageEvent(event);
		        document.open();
		        if(!(report_heading.getText().equals(""))){
                document.add(new Phrase(report_heading.getText()+"\n",new Font(Font.FontFamily.UNDEFINED, 18, Font.BOLD|Font.UNDERLINE)));
		        }
		        if(!(details.getText().equals(""))){
                document.add(new Phrase(details.getText()+"\n"));
		        }
                
		        PdfPTable table = new PdfPTable(new float[] { 1, 5, 2, 2, 2, 2 });
                  table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
                  Font tfont = new Font(Font.FontFamily.UNDEFINED, 12, Font.BOLD);
                  Font dfont = new Font(Font.FontFamily.UNDEFINED, 10);
                  table.setWidthPercentage(100);
                  table.addCell(new Phrase("ID",tfont));
            	  table.addCell(new Phrase("Name",tfont));
            	  table.addCell(new Phrase("Date",tfont));
            	  table.addCell(new Phrase("Money In",tfont));
            	  table.addCell(new Phrase("Money Out",tfont));
            	  table.addCell(new Phrase("Balance",tfont));

                  while (result.next()) {    
                	  String name = (result.getString(2) == null) ? result.getString(3):result.getString(2);
                	  table.addCell(new Phrase(result.getString(1), dfont));
                	  table.addCell(new Phrase(name, dfont));
                	  table.addCell(new Phrase(result.getString(7), dfont));
                	  table.addCell(new Phrase(result.getString(4), dfont));
                	  table.addCell(new Phrase(result.getString(5), dfont));
                	  table.addCell(new Phrase(result.getString(6), dfont));
                  }
                  
                  document.add(table);
                  document.add(new Phrase("\n \n \n Total Money In  : "+moneyin.toString()));
                  document.add(new Phrase("\n Total Money Out : "+moneyout.toString()));
                  document.close();
                  
                  File myFile = new File("Report.pdf");
                  Desktop.getDesktop().open(myFile);
                  result.close();
                  statement.close(); 
                  con.close();

    	}
    	catch(Exception e){
    		System.out.println(e);
    	}
    }	
}