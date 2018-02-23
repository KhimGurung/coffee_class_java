package FeePayment;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NumberToStringConversionTest {
	InvoiceController newobj;
	
	@Before
	public void setup(){
		newobj = new InvoiceController();
	}
	
	@Test
	public void test() {
		String num_in_figure = newobj.convert(100);
		assertEquals(num_in_figure,"One Hundred ");
	}
	
	public void DataFetchFromDatabaseTest(){
		
	}

}
