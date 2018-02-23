package FeePayment;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import dbconnection.ConnectionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class InvoiceController {
	 private Integer id,debit;
	 private String reqchar,name,address,joindate;
 	String string;
 	String st1[] = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven",
 			"Eight", "Nine", };
 	String st2[] = { "Hundred", "Thousand", "Lakh", "Crore" };
 	String st3[] = { "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen",
 			"Fifteen", "Sixteen", "Seventeen", "Eighteen", "Ninteen", };
 	String st4[] = { "Twenty", "Thirty", "Fourty", "Fifty", "Sixty", "Seventy",
 			"Eighty", "Ninty" };
 	 @FXML
     private Button print_invoice;

     @FXML
     private Label invo_name,invo_course,invo_fee,invo_word,invo_time,invo_date,invo_id,invo_no,invo_address,invo_join_date;

	void setData(Integer id, Integer debit, String reqchar){
		this.id=id;
		this.debit=debit;
		this.reqchar=reqchar;
		
		try{
			Connection con = ConnectionManager.getConnection();
			String sql="SELECT std_name, std_address, join_date "+
						"FROM student "+
						"WHERE std_id = "+id+";";
			PreparedStatement statement=con.prepareStatement(sql);
			ResultSet result=statement.executeQuery();
				while(result.next()){
					name=result.getString(1);
					address =result.getString(2);
					joindate =result.getString(3);
				}
			}catch(SQLException ex){
				System.err.println("Error"+ex);
			}
	}
    @FXML
	void create_invoice(ActionEvent events) throws DocumentException, IOException  {    
         invoice();
    }
    
	public void invoice() throws DocumentException, IOException{
		 Rectangle pgSize = new Rectangle(550, 270);
		 Document document = new Document(pgSize, 30,30, 90,0);
		 
	        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("invoice/"+reqchar.toUpperCase()+".pdf"));
	        // add header and footer
	        InvoiceStyle event = new InvoiceStyle();
	        BaseFont receipt_font = BaseFont.createFont("andalus.ttf",BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
	        Font dfont = new Font(receipt_font , 14);
	        Font ufont = new Font(receipt_font , 14, Font.BOLD|Font.UNDERLINE);
	        writer.setPageEvent(event);

	        document.open();
	        
	        PdfPTable invoiceno = new PdfPTable(2);
	        invoiceno.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
	        invoiceno.setWidths(new int[]{1,1});
	        invoiceno.setTotalWidth(490);
	        invoiceno.setLockedWidth(true);
	        invoiceno.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	        invoiceno.addCell(new Phrase("Student ID : "+id, dfont));

	        invoiceno.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	        invoiceno.addCell(new Phrase("Receipt No. "+reqchar.toUpperCase(), dfont));
	        document.add(invoiceno);
	        
            Phrase text = new Phrase(25);
            text.add(new Chunk("Received from",dfont));
            text.add(new Chunk(" "+name+", ", ufont));
            text.add(new Chunk("on behalf of ", dfont));
            text.add(new Chunk(" Coffee Making Training ", ufont));
            text.add(new Chunk("the sum of Rs. ", dfont));
            text.add(new Chunk(" "+debit.toString()+" ",ufont));
            text.add(new Chunk(" in words ", dfont));
            text.add(new Chunk(" "+convert(debit)+".",ufont));
	        Paragraph p = new Paragraph(text);
	        p.setAlignment(Element.ALIGN_JUSTIFIED);
	        document.add(p);
	        
	        PdfPTable invoadd = new PdfPTable(2);
	        invoadd.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
	        invoadd.setWidths(new int[]{2,1});
	        invoadd.setTotalWidth(490);
	        invoadd.setLockedWidth(true);
	        invoadd.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	        invoadd.addCell(new Phrase("\n\nAddress : "+address+"\nJoin Date : "+joindate, dfont));
	        
	        invoadd.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	        invoadd.addCell(new Phrase("\n\nSign By Receiver _____________", dfont));
	        document.add(invoadd);
	        
	        document.add(new Phrase("--------------------------------------------------------------------------------------------------------------------------"));
	        document.close();
	        File myFile = new File("invoice/"+reqchar.toUpperCase()+".pdf");
            try {
				Desktop.getDesktop().open(myFile);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        /*FileInputStream psStream = null;
	        try {
	            psStream = new FileInputStream("Invoice.pdf");
	            } catch (FileNotFoundException ffne) {
	              ffne.printStackTrace();
	            }
	            if (psStream == null) {
	                return;
	            }
	        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
	        Doc myDoc = new SimpleDoc(psStream, psInFormat, null);  
	        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
	        PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, aset);
	         
	        // this step is necessary because I have several printers configured
	        PrintService myPrinter = null;
	        for (int i = 0; i < services.length; i++){
	            String svcName = services[i].toString();         
	            if (svcName.contains("printer closest to me")){
	                myPrinter = services[i];
	                break;
	            }
	        }
	         
	        if (myPrinter != null) {            
	            DocPrintJob job = myPrinter.createPrintJob();
	            try {
	            job.print(myDoc, aset);
	             
	            } catch (Exception pe) {pe.printStackTrace();}
	        } else {
	        	Alert alert = new Alert(AlertType.INFORMATION);
	        	alert.setTitle("Information");
	        	alert.setHeaderText("No printer services found");
	        	alert.showAndWait();
	        }*/
	}
    	public String convert(int number) {
    		int n = 1;
    		int word;
    		string = "";
    		while (number != 0) {
    			switch (n) {
    			case 1:
    				word = number % 100;
    				pass(word);
    				if (number > 100 && number % 100 != 0) {
    					show("And ");
    				}
    				number /= 100;
    				break;

    			case 2:
    				word = number % 10;
    				if (word != 0) {
    					show(" ");
    					show(st2[0]);
    					show(" ");
    					pass(word);
    				}
    				number /= 10;
    				break;

    			case 3:
    				word = number % 100;
    				if (word != 0) {
    					show(" ");
    					show(st2[1]);
    					show(" ");
    					pass(word);
    				}
    				number /= 100;
    				break;

    			case 4:
    				word = number % 100;
    				if (word != 0) {
    					show(" ");
    					show(st2[2]);
    					show(" ");
    					pass(word);
    				}
    				number /= 100;
    				break;

    			case 5:
    				word = number % 100;
    				if (word != 0) {
    					show(" ");
    					show(st2[3]);
    					show(" ");
    					pass(word);
    				}
    				number /= 100;
    				break;

    			}
    			n++;
    		}
    		return string;
    	}

    	public void pass(int number) {
    		int word, q;
    		if (number < 10) {
    			show(st1[number]);
    		}
    		if (number > 9 && number < 20) {
    			show(st3[number - 10]);
    		}
    		if (number > 19) {
    			word = number % 10;
    			if (word == 0) {
    				q = number / 10;
    				show(st4[q - 2]);
    			} else {
    				q = number / 10;
    				show(st1[word]);
    				show(" ");
    				show(st4[q - 2]);
    			}
    		}
    	}

    	public void show(String s) {
    		String st;
    		st = string;
    		string = s;
    		string += st;
    	}
    }
