package NewStudent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import dbconnection.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class NewStudentController implements Initializable {
	public String newName,oldName;
	public String photoname="default.jpg";
	
	@FXML
	public Label newstdinfo;
	public Label student_id;
	public Label status;
	public Label hidden_id;
	public Label std_hidden_id;
	
	@FXML
	public TextField country;
	public TextField email;
	public TextField contact_no;
	public TextField search_keyword;
	public TextField stdname;
	public TextField stdaddress;
	public TextField fee;
	
	@FXML
	public ImageView std_image;

	@FXML
	public FileChooser filechooser = new FileChooser();

	@FXML 
	public ComboBox<String> newgendercombobox;
	public ComboBox<String> search_type;
	
	@FXML
	public Button save_std;
	public Button update_std;
	public Button delete_std;
	public Button add_new_std;
	public Button choose_image;
	
	@FXML
	public Pane btn_grp;
	public Pane heading;
	
	@FXML
	public DatePicker finishdate, joindate;
	
	public File file;
	public FileInputStream fis;
	
	public PreparedStatement statement;
	
	
	@FXML private TableView<Student> std_list;
	@FXML private TableColumn<Student, Integer> id;
	@FXML private TableColumn<Student, String> name;
	@FXML private TableColumn<Student, String> address;
	@FXML private TableColumn<Student, String> std_gender;
	@FXML private TableColumn<Student, String> std_photo;
	@FXML private TableColumn<Student, String> std_contact;
	@FXML private TableColumn<Student, String> join_date;	
	@FXML private TableColumn<Student, String> finish_date;	
	@FXML private TableColumn<Student, Integer> total_fee;
	
	public ObservableList<Student> stdlist;
	
	ArrayList<String> oldrecord=new ArrayList<String>();
	ArrayList<String> newrecord=new ArrayList<String>();
	
	ObservableList<String> gender = FXCollections.observableArrayList("Male","Female");
	ObservableList<String> search = FXCollections.observableArrayList("Student ID","Name","Address","Gender","Contact No.","Join Date");
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		RefreshTable();
		setCellValueFromTableToTextField();
		
		/* setting items for combobox gender and search type */
		newgendercombobox.setItems(gender);
		search_type.setItems(search);
	}
	 
	@FXML
	private void save_student_detail(ActionEvent event) throws SQLException, IOException {
		 	if(EmptyFieldValidation()){
				String name=stdname.getText();
				String address=stdaddress.getText();
				String gender=newgendercombobox.getValue();
				String std_contact_no= contact_no.getText();
				Integer feee = Integer.parseInt(fee.getText());

				LocalDate jdate=joindate.getValue();
				LocalDate fdate=finishdate.getValue();
				StudentModel ObjSave = new StudentModel();
				if(ObjSave.SaveNewStudentRecord(name, address, gender, std_contact_no, photoname, jdate, fdate, feee)){
					status.setText("New student Registered !!!");
					File dest = new File("C:/Users/KHIM/workspace/coffee_culture/std_photo/"+ photoname); 
			        if(photoname != "default.jpg"){
					Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
			        }
					RefreshTable();
					ClearAllControls();
				}else{
					status.setText("Resistration Failed !!!");
				}
		 	}
	    }
	 
	public void update_student(ActionEvent event) throws SQLException, IOException {
		 
			Integer id=Integer.parseInt(hidden_id.getText());
			String name = stdname.getText();
			String address = stdaddress.getText();
			String gender = newgendercombobox.getValue();
			String photo = choose_image.getText();
			String std_contact_no = contact_no.getText();
			String join_date = joindate.getValue().toString();
			String finish_date = finishdate.getValue().toString();
			String totalfee= fee.getText();
			
			newrecord.clear();
			newrecord.add(stdname.getText());
			newrecord.add(stdaddress.getText());
			newrecord.add(newgendercombobox.getValue());
			newrecord.add(choose_image.getText());
			newrecord.add(contact_no.getText());
			newrecord.add(joindate.getValue().toString());
			newrecord.add(finishdate.getValue().toString());
			newrecord.add(fee.getText());
		 
		 	if(EmptyFieldValidation() && change_made_check()){
				
		 		StudentModel ObjUpdate = new StudentModel();
		 		if(ObjUpdate.UpdateStudentRecord(name, address, gender, photo, std_contact_no ,join_date ,finish_date, totalfee, id)){
					if(choose_image.getText().equals("") || choose_image.getText().equals("default.jpg") || choose_image.getText().equals(oldName)){
					
					}
					else{
						File filee = new File("C:/Users/KHIM/workspace/coffee_culture/std_photo/"+ oldName);
						filee.delete();
						File dest = new File("C:/Users/KHIM/workspace/coffee_culture/std_photo/"+ newName); //any location
				        Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
					}
			        status.setText("student Detail Updated !!!");
			    	stdname.setPromptText("");
			    	stdaddress.setPromptText("");
			    	contact_no.setPromptText("");
			    	finishdate.setPromptText("");
			    	joindate.setPromptText("");
			    	newgendercombobox.setPromptText("Select Gender");
			    	choose_image.setText("Choose Image");
			    	fee.setPromptText("");
			    	std_image.setImage(new Image("file:///C:/Users/KHIM/workspace/coffee_culture/std_photo/default.jpg",116,120,true,true));
					RefreshTable();
					ClearAllControls();
				}else{
					status.setText("Update Failed !!!");
				}
		 	}
	    }
	 
	public void delete_student(ActionEvent event) throws SQLException {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("You are going to delete a record of a student.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			
			Integer id=Integer.parseInt(hidden_id.getText());
			String delete_image = choose_image.getText();
			
			StudentModel ObjDelete = new StudentModel();
			if(ObjDelete.DeleteStudent(id)){
				if(photoname != "default.jpg"){
					File filee = new File("C:/Users/KHIM/workspace/coffee_culture/std_photo/"+ delete_image);
					filee.delete();
					std_image.setImage(new Image("file:///C:/Users/KHIM/workspace/coffee_culture/std_photo/default.jpg",116,120,true,true));
					}
					status.setText("Record Deleted !!!");
					RefreshTable();
					ClearAllControls();
			}else{
				status.setText("Record cannot be delete.");
			}
		}
	}

	private void RefreshTable(){
		try{
			stdlist=FXCollections.observableArrayList();
			StudentModel ObjFetch = new StudentModel();
			ResultSet result=ObjFetch.FetchAllStudent();
			while(result.next()){
				stdlist.add(new Student(result.getInt(1),result.getString(2),result.getString(3),result.getString(4),result.getString(5),result.getString(6),result.getString(7),result.getString(8),result.getInt(9)));
			}
			}catch(SQLException ex){
				System.err.println("Error"+ex);
			}

			id.setCellValueFactory(new PropertyValueFactory<Student,Integer>("std_id"));
			name.setCellValueFactory(new PropertyValueFactory<Student,String>("std_name"));
			address.setCellValueFactory(new PropertyValueFactory<Student,String>("std_address"));
			std_gender.setCellValueFactory(new PropertyValueFactory<Student,String>("std_gender"));
			std_contact.setCellValueFactory(new PropertyValueFactory<Student,String>("std_contact_no"));
			std_photo.setCellValueFactory(new PropertyValueFactory<Student,String>("std_photo"));
			join_date.setCellValueFactory(new PropertyValueFactory<Student,String>("join_date"));
			finish_date.setCellValueFactory(new PropertyValueFactory<Student,String>("finish_date"));
			total_fee.setCellValueFactory(new PropertyValueFactory<Student, Integer>("total_fee"));
			std_list.setItems(stdlist);
	}
	private boolean EmptyFieldValidation(){
		if(stdname.getText().equals("") || stdaddress.getText().equals("") || contact_no.getText().equals("") || newgendercombobox.getSelectionModel().isEmpty() || joindate.getValue().equals(null) || finishdate.getValue().equals(null) || fee.getText().equals("")){
			if(stdname.getText().equals("")){
				stdname.setPromptText("student name is mandatory.");
			}
			if(stdaddress.getText().equals("")){
				stdaddress.setPromptText("Address is mandatory.");
			}
			if(contact_no.getText().equals("")){
				contact_no.setPromptText("Contact number is mandatory.");
			}
			if(newgendercombobox.getSelectionModel().isEmpty()){
				newgendercombobox.setPromptText("Gender is mandatory.");
			}
			if(joindate.getValue() == null){
				joindate.setPromptText("Join date is mandatory.");
			}
			if(finishdate.getValue() == null){
				finishdate.setPromptText("Finish date is mandatory.");
			}
			if(fee.getText().equals("") || (Integer.parseInt(fee.getText())<10000)){
				fee.setPromptText("Fee is mandatory.");
			}
				return false;
		}else{
		return true;
		}
	 }
	
    @FXML
    void add_new_std(ActionEvent event) {
    	stdname.setPromptText("");
    	stdaddress.setPromptText("");
    	contact_no.setPromptText("");
    	joindate.setPromptText("");
    	finishdate.setPromptText("");
    	newgendercombobox.setPromptText("Select Gender");
    	fee.setPromptText("");
    	ClearAllControls();
    }
    
	private void ClearAllControls(){
		stdname.clear();
		stdaddress.clear();
		contact_no.clear();
		newgendercombobox.setValue(null);
		hidden_id.setText("----");
		joindate.setValue(null);
		finishdate.setValue(null);
		fee.clear();
		choose_image.setText("Choose Image");
    	std_image.setImage(new Image("file:///C:/Users/KHIM/workspace/coffee_culture/std_photo/default.jpg",116,120,true,true));
		
		save_std.setDisable(false);
		update_std.setDisable(true);
		delete_std.setDisable(true);
	}
	
	private void setCellValueFromTableToTextField(){
		std_list.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				if(event.getClickCount() == 2){ 
				Student sc=std_list.getItems().get(std_list.getSelectionModel().getSelectedIndex());
				
				hidden_id.setText(sc.getStd_id().toString());
				stdname.setText(sc.getStd_name());
				stdaddress.setText(sc.getStd_address());
				newgendercombobox.setValue(sc.getStd_gender());
				contact_no.setText(sc.getStd_contact_no());
				joindate.setValue(LocalDate.parse(sc.getJoin_date()));
				finishdate.setValue(LocalDate.parse(sc.getFinish_date()));
				choose_image.setText(sc.getStd_photo());
				fee.setText(sc.getTotal_fee().toString());
				oldName=sc.getStd_photo();
				std_image.setImage(new Image("file:///C:/Users/KHIM/workspace/coffee_culture/std_photo/"+sc.getStd_photo()));
				
				
				save_std.setDisable(true);
				update_std.setDisable(false);
				delete_std.setDisable(false);
				status.setText("");
				
				oldrecord.clear();
				oldrecord.add(sc.getStd_name());
				oldrecord.add(sc.getStd_address());
				oldrecord.add(sc.getStd_gender());
				oldrecord.add(sc.getStd_photo());
				oldrecord.add(sc.getStd_contact_no());
				oldrecord.add(sc.getJoin_date());
				oldrecord.add(sc.getFinish_date());
				oldrecord.add(sc.getTotal_fee().toString());
				}
			}
		});
	}

	@FXML
	String searchtype,searchsql;
	public void search_student(KeyEvent event) {
		String searchkeyword=search_keyword.getText();
		if(search_type.getValue().equals("Student ID")){
			if(search_keyword.getText().equals("")){
				searchsql="SELECT * FROM student ORDER BY std_id DESC";
			}
			else{
				searchsql="SELECT * FROM student WHERE std_id = "+searchkeyword+" ORDER BY std_id DESC";
			}
		}
		else {
			if(search_type.getValue().equals("Name")){
				 searchtype="std_name";
			}
			else if(search_type.getValue().equals("Address")){
				 searchtype="std_address";
			}
			else if(search_type.getValue().equals("Gender")){
				 searchtype="std_gender";
			}
			else if(search_type.getValue().equals("Contact No.")){
				 searchtype="std_contact_no";
			}
			else{
				 searchtype="join_date";
			}
			searchsql="SELECT * FROM student WHERE "+searchtype+" LIKE '"+searchkeyword+"%' ORDER BY std_id DESC";
		}
		
		try{
			Connection con = ConnectionManager.getConnection();
			stdlist=FXCollections.observableArrayList();
			PreparedStatement statement=con.prepareStatement(searchsql);
			ResultSet result=statement.executeQuery();
			while(result.next()){
				stdlist.add(new Student(result.getInt(1),result.getString(2),result.getString(3),result.getString(4),result.getString(5),result.getString(6),result.getString(7),result.getString(8),result.getInt(9)));
			}
			}catch(SQLException ex){
				System.err.println("Error"+ex);
			}

		id.setCellValueFactory(new PropertyValueFactory<Student,Integer>("std_id"));
		name.setCellValueFactory(new PropertyValueFactory<Student,String>("std_name"));
		address.setCellValueFactory(new PropertyValueFactory<Student,String>("std_address"));
		std_gender.setCellValueFactory(new PropertyValueFactory<Student,String>("std_gender"));
		std_contact.setCellValueFactory(new PropertyValueFactory<Student,String>("std_contact_no"));
		std_photo.setCellValueFactory(new PropertyValueFactory<Student,String>("std_photo"));
		join_date.setCellValueFactory(new PropertyValueFactory<Student,String>("join_date"));
		finish_date.setCellValueFactory(new PropertyValueFactory<Student,String>("finish_date"));
		total_fee.setCellValueFactory(new PropertyValueFactory<Student, Integer>("total_fee"));
		std_list.setItems(stdlist);
	 }
	
	 @FXML
	 void openWindow(ActionEvent event) {
		 filechooser.getExtensionFilters().addAll(
				 new ExtensionFilter("Image Files","*.png","*.jpg"));
		 file=filechooser.showOpenDialog(null);
		 
		 if(file != null){
			 UUID uniqueid=UUID.randomUUID();
	         newName = uniqueid.toString() + ".jpg";
	         choose_image.setText(newName);
			 photoname=newName;
			 std_image.setImage(new Image(file.toURI().toString(),116,120,true,true));
		 }
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
		    	if(!(Character.isDigit(input) || Character.isSpaceChar(input) || Character.toString(input).equals(""))){
		    		/*Toolkit.getDefaultToolkit().beep();*/
		    		event.consume();
		    	}
	 }
	 
	 private boolean change_made_check(){
			 if( Arrays.equals(oldrecord.toArray(),newrecord.toArray())){
				 status.setText("No any changes are made.");
				 return false;
			 }
			 else{
				 return true;
			 }
	 }
	
}
