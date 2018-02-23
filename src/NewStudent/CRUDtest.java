package NewStudent;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.Test;

import dbconnection.ConnectionManager;

public class CRUDtest {

	@Test
	public void testCRUD() throws SQLException {
		StudentModel saveData = new StudentModel();
		Connection con = ConnectionManager.getConnection();
		int size = 0;
		String name = "Ram Gurung";
		String address = "Frankfurt";
		String photoname = "Ram Gurung";
		String gender = "Male";
		String std_contact_no = "977 9806845634";
		Integer feee = 15000;
		LocalDate jdate = LocalDate.now();
		LocalDate fdate = LocalDate.now();
		
		Integer id = 40;
		
		//testing "FetchAllStudent()" method
		ResultSet FetchResult = saveData.FetchAllStudent();
		while(FetchResult.next()){
			size=FetchResult.getRow();
		}
		assertEquals(size, 9);
		
		//testing "SaveNewStudentRecord" method
		boolean SaveResult = saveData.SaveNewStudentRecord(name, address, gender, std_contact_no, photoname, jdate, fdate, feee);
		assertEquals(SaveResult, true); //function returns true if record is added
		
		FetchResult = saveData.FetchAllStudent();
		while(FetchResult.next()){
			size=FetchResult.getRow();
		}
		assertEquals(10, size); //number of record should increased by 1 i.e should be 10 if new record is added
		
		address="Fulda";
		//testing "UpdateStudentRecord" method
		boolean UpdateResult = saveData.UpdateStudentRecord(name, address, gender, photoname, std_contact_no, jdate.toString(), fdate.toString(), feee.toString(), id);
		assertEquals(UpdateResult, true); //function returns true if record is updated
		
		PreparedStatement statement=con.prepareStatement("SELECT * FROM student WHERE std_id="+id);
		ResultSet result=statement.executeQuery();
		while(result.next()){
			assertEquals(result.getString(3),"Fulda"); //checking if address is updated to "Fulda" or not
		}
		
		//testing "DeleteStudent" method
		boolean DeleteResult = saveData.DeleteStudent(id);
		assertEquals(DeleteResult, true); //returns true if record of given id is deleted
	}

}
