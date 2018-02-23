package dbconnection;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.Test;

public class dbconnectionTest {

	@Test
	public void test() {
		boolean result = true;
		Connection con = ConnectionManager.getConnection();
		assertEquals(con != null, result);
	}

}
