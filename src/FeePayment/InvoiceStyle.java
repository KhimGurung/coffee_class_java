package FeePayment;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class InvoiceStyle extends PdfPageEventHelper {

	    @Override
	    public void onEndPage(PdfWriter writer, Document document) {
    	LocalDate localDate = LocalDate.now();
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a");
    	String localtime = LocalTime.now().format(dtf);
    	PdfPTable header = new PdfPTable(3);
        try {
            // set defaults
            header.setWidths(new int[]{4,14,7});
            header.setTotalWidth(510);
            header.setLockedWidth(true);
            header.getDefaultCell().setBorder(Rectangle.BOTTOM);
            header.getDefaultCell().setBorderWidth(1);
            header.getDefaultCell().setBorderColor(BaseColor.BLACK);

            // add image
    		Image logo = Image.getInstance("img/logo.png");
            header.addCell(logo);

            // add text
            BaseFont receipt_font = BaseFont.createFont("andalus.ttf",BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            Phrase text = new Phrase(10);
            text.add(new Chunk("Coffee Culture",new Font(receipt_font, 30, Font.BOLD)));
            text.add(new Chunk("\nNew Road, Pokhara, Nepal", new Font(receipt_font, 16)));
            text.add(new Chunk("\nPhone: 061-522427", new Font(receipt_font, 12)));
	        Paragraph p = new Paragraph(text);
	        p.setAlignment(Element.ALIGN_LEFT);
            header.addCell(p);
            
            PdfPCell date = new PdfPCell();
            date.setPaddingLeft(10);
            date.setBorder(Rectangle.BOTTOM);
            date.setBorderWidth(1);
            date.setBorderColor(BaseColor.BLACK);
            date.addElement(new Phrase(" "));
            date.addElement(new Phrase("Received Date: "+localDate.toString(), new Font(receipt_font, 11)));
            date.addElement(new Phrase("Received Time: "+localtime.toString(), new Font(receipt_font, 11)));
            header.addCell(date);

            // write content
            header.writeSelectedRows(0, -1, 20, 260, writer.getDirectContent());
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        } catch (MalformedURLException e) {
            throw new ExceptionConverter(e);
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }   
}