package br.com.gotech.docs;

import java.io.IOException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFBackground extends PdfPageEventHelper {

	private String url = this.getClass().getResource("/BOOT-INF/classes/templates/docs/recibo.jpg").toString();

	// private String url =
	// "C:\\Users\\User\\Documents\\workspace\\STS\\locafacil-api\\src\\main\\resources\\templates\\docs\\recibo.jpg";

	@Override
	public void onEndPage(PdfWriter writer, Document document) {

		Image background = null;

		try {

			background = Image.getInstance(url);

		} catch (BadElementException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// This scales the image to the page,
		// use the image's width & height if you don't want to scale.
		float width = document.getPageSize().getWidth();
		float height = document.getPageSize().getHeight();
		try {
			writer.getDirectContentUnder().addImage(background, width, 0, 0, height, 35, -10);
			writer.getDirectContentUnder().addImage(background, width, 0, 0, height, 35, -400);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}