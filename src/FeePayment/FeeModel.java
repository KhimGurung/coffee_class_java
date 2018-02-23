package FeePayment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import dbconnection.ConnectionManager;

public class FeeModel {
	
	PreparedStatement statement;
	//Saving new student record
		public boolean SaveNewStudentRecord(Integer id, Integer debit, Integer balance, String reqchar) throws SQLException{
			Connection con = ConnectionManager.getConnection();
			String sql="INSERT INTO student(std_id,std_name,std_address,std_gender,std_contact_no,std_photo,join_date,finish_date,fee) VALUES(?,?,?,?,?,?,?,?,?)";
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
			if(i == 1){
				return true;
			}
			return false;
		}
}
