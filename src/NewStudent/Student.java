package NewStudent;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student {
	private SimpleIntegerProperty std_id;
	private SimpleStringProperty std_name;
	private SimpleStringProperty std_address;
	private SimpleStringProperty std_gender;
	private SimpleStringProperty std_photo;
	private SimpleStringProperty std_contact_no;
	private SimpleStringProperty join_date;
	private SimpleStringProperty finish_date;
	private SimpleIntegerProperty total_fee;
	public Student(Integer std_id,String std_name, String std_address, String std_gender,String std_photo, String std_contact_no,
			String join_date, String finish_date, Integer total_fee) {
		super();
		this.std_id = new SimpleIntegerProperty(std_id);
		this.std_name = new SimpleStringProperty(std_name);
		this.std_address = new SimpleStringProperty(std_address);
		this.std_gender = new SimpleStringProperty(std_gender);
		this.std_photo = new SimpleStringProperty(std_photo);
		this.std_contact_no = new SimpleStringProperty(std_contact_no);
		this.join_date = new SimpleStringProperty(join_date);
		this.finish_date = new SimpleStringProperty(finish_date);
		this.total_fee = new SimpleIntegerProperty(total_fee);
	}
	public Integer getStd_id() {
		return std_id.get();
	}
	public String getStd_name() {
		return std_name.get();
	}
	public String getStd_address() {
		return std_address.get();
	}
	public String getStd_gender() {
		return std_gender.get();
	}
	public String getStd_photo() {
		return std_photo.get();
	}
	public String getStd_contact_no() {
		return std_contact_no.get();
	}
	public String getJoin_date() {
		return join_date.get();
	}
	public String getFinish_date() {
		return finish_date.get();
	}
	public Integer getTotal_fee() {
		return total_fee.get();
	}	
	
}
