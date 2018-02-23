package FeePayment;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Fee {
	private final SimpleIntegerProperty trans_id;
	private final SimpleStringProperty name;
	private final SimpleStringProperty date;
	private final SimpleIntegerProperty amount;
	private final SimpleStringProperty invoice;
	
	public Fee(Integer trans_id, String name, String date, Integer amount, String invoice) {
		super();
		this.trans_id = new SimpleIntegerProperty(trans_id);
		this.name = new SimpleStringProperty(name);
		this.date = new SimpleStringProperty(date);
		this.amount = new SimpleIntegerProperty(amount);
		this.invoice = new SimpleStringProperty(invoice);
	}

	public Integer getTrans_id() {
		return trans_id.get();
	}
	public String getName() {
		return name.get();
	}
	public String getDate() {
		return date.get();
	}
	public Integer getAmount() {
		return amount.get();
	}
	public String getInvoice() {
		return invoice.get();
	}
}
