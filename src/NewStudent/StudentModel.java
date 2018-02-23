package NewStudent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import dbconnection.ConnectionManager;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class StudentModel {
	
	public PreparedStatement statement;
	
	//delteing record of giving ID
	public boolean DeleteStudent(Integer id) throws SQLException{
		Connection con = ConnectionManager.getConnection();
		
		PreparedStatement statement=con.prepareStatement("SELECT * FROM transaction WHERE std_id = "+id.toString());
		ResultSet resultt=statement.executeQuery();
		if(!resultt.next()){
			String sql="DELETE FROM student WHERE std_id="+id;
		try{
			statement = con.prepareStatement(sql);
			int i=statement.executeUpdate();
			if(i == 1){
				return true;
			}
		}
		catch(SQLException ex){
				System.err.println("Error"+ex);
		}
		finally{
				statement.close();
			}
		} 
		else{
			Alert alert1 = new Alert(AlertType.INFORMATION);
        	alert1.setTitle("Information");
        	alert1.setHeaderText("Record cannot be deleted because there is transaction of this student.");
        	alert1.showAndWait();
		}
		return false;
	}
	
	//Reading all records of students
	public ResultSet FetchAllStudent() throws SQLException{
		Connection con = ConnectionManager.getConnection();
		PreparedStatement statement=con.prepareStatement("SELECT * FROM student ORDER BY std_id DESC");
		ResultSet result=statement.executeQuery();
		return result;
	}
	
	//Updating student record
	public boolean UpdateStudentRecord(String name, String address, String gender, String photo, String std_contact_no ,
									String join_date ,String finish_date, String totalfee, Integer id) throws SQLException{
		Connection con = ConnectionManager.getConnection();
		String sql="UPDATE  student SET std_name = ?, std_address = ?, std_gender = ?, std_photo= ?, std_contact_no = ?, join_date = ?, finish_date = ?, fee = ? WHERE std_id = ?";
	try{
		statement = con.prepareStatement(sql);
		statement.setString(1, name);
		statement.setString(2, address);
		statement.setString(3, gender);
		statement.setString(4, photo);
		statement.setString(5, std_contact_no);
		statement.setString(6, join_date);
		statement.setString(7, finish_date);
		statement.setString(8, totalfee);
		statement.setInt(9, id);
		
		int i=statement.executeUpdate();
		if(i == 1){
			return true;
		}
	}
	catch(SQLException ex){
			System.err.println("Error"+ex);
	}
	finally{
			statement.close();
		}
	return false;
	}
	
	//Saving new student record
	public boolean SaveNewStudentRecord(String name, String address, String gender, String std_contact_no, 
										String photoname, LocalDate jdate, LocalDate fdate, Integer feee) throws SQLException{
		Connection con = ConnectionManager.getConnection();
		String sql="INSERT INTO student(std_id,std_name,std_address,std_gender,std_contact_no,std_photo,join_date,finish_date,fee) VALUES(?,?,?,?,?,?,?,?,?)";
		statement = con.prepareStatement(sql);
		statement.setDouble(1, 0);
		statement.setString(2,name);
		statement.setString(3,address);
		statement.setString(4,gender);
		statement.setString(5,std_contact_no);
		statement.setString(6,photoname);
		statement.setString(7,jdate.toString());
		statement.setString(8,fdate.toString());
		statement.setInt(9,feee);
		
		int i=statement.executeUpdate();
		if(i == 1){
			return true;
		}
		return false;
	}
}
