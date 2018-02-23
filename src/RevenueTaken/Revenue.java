package RevenueTaken;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Revenue {
	private final SimpleIntegerProperty trans_id;
	private final SimpleStringProperty name;
	private final SimpleStringProperty date;
	private final SimpleIntegerProperty amount;
	
	public Revenue(Integer trans_id,String name, String date, Integer amount) {
		super();
		this.trans_id = new SimpleIntegerProperty(trans_id);
		this.name = new SimpleStringProperty(name);
		this.date = new SimpleStringProperty(date);
		this.amount = new SimpleIntegerProperty(amount);
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
}
