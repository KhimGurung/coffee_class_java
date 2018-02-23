package TransactionReport;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Transaction {
	private final SimpleIntegerProperty trans_id;
	private final SimpleStringProperty std_name;
	private final SimpleStringProperty share_name;
	private final SimpleIntegerProperty debit;
	private final SimpleIntegerProperty credit;
	private final SimpleIntegerProperty balance;
	private final SimpleStringProperty date;
	public Transaction(Integer trans_id,String std_name, String share_name, Integer debit, Integer credit,Integer balance, String date) {
		super();
		this.trans_id = new SimpleIntegerProperty(trans_id);
		this.std_name = new SimpleStringProperty(std_name);
		this.share_name = new SimpleStringProperty(share_name);
		this.debit = new SimpleIntegerProperty(debit);
		this.credit = new SimpleIntegerProperty(credit);
		this.balance = new SimpleIntegerProperty(balance);
		this.date = new SimpleStringProperty(date);
	}
	public Integer getTrans_id() {
		return trans_id.get();
	}
	public String getStd_name() {
		return std_name.get();
	}
	public String getShare_name() {
		return share_name.get();
	}
	public Integer getDebit() {
		return debit.get();
	}
	public Integer getCredit() {
		return credit.get();
	}
	public Integer getBalance() {
		return balance.get();
	}
	public String getDate() {
		return date.get();
	}
	
}
