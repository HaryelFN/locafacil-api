package br.com.gotech.docs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import br.com.gotech.model.Aluguel;
import br.com.gotech.service.ContratoService;
import br.com.gotech.util.Convert;

public class GerarRecibo {

	static LocalDate DATE_NOW = LocalDate.now();

	public static ByteArrayInputStream gerar(Aluguel aluguel) {

		Document document = new Document(PageSize.A4, 20, 20, 20, 20);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {

			PdfWriter writer = PdfWriter.getInstance(document, out);

			document.open();
			addMetaData(document);

			addTextData(writer, String.format("%05d", aluguel.getId()), 90, 755, 13);
			addTextData(writer, Convert.floatToCurrency(aluguel.getValor()), 370, 755, 13);
			addTextData(writer, aluguel.getContrato().getLocatario1().getNome(), 132, 719, 13);
			addTextData(writer, Convert.floatPorExtenso(aluguel.getValor()).toUpperCase() + " REAIS", 135, 693, 13);

			addTextData(writer,
					"aluguel, endereço " + aluguel.getContrato().getImovel().getLogradouro() + ", "
							+ aluguel.getContrato().getImovel().getCidade() + ",\n "
							+ aluguel.getContrato().getImovel().getBairro() + ",\n "
							+ aluguel.getContrato().getImovel().getUf() + ".",
					138, 643, 11);

			addTextData(writer,
					"referente ao mês de " + Convert.localDateToString(aluguel.getDtVencimento())
							.substring(5, Convert.localDateToString(aluguel.getDtVencimento()).length()).toLowerCase()
							+ ".",
					57, 627, 11);

			addTextData(writer, "ASS: ", 60, 575, 13);
			addTextData(writer, "Data " + Convert.localDateToString(DATE_NOW), 346, 540, 12);

			addTextData(writer, String.format("%05d", aluguel.getId()), 90, 365, 13);
			addTextData(writer, Convert.floatToCurrency(aluguel.getValor()), 370, 365, 13);
			addTextData(writer, aluguel.getContrato().getLocatario1().getNome(), 132, 330, 13);
			addTextData(writer, Convert.floatPorExtenso(aluguel.getValor()).toUpperCase() + " REAIS", 135, 301, 13);

			addTextData(writer,
					"aluguel, endereço " + aluguel.getContrato().getImovel().getLogradouro() + ", "
							+ aluguel.getContrato().getImovel().getCidade() + ",\n "
							+ aluguel.getContrato().getImovel().getBairro() + ",\n "
							+ aluguel.getContrato().getImovel().getUf() + ".",
					138, 254, 11);

			addTextData(writer,
					"referente ao mês de " + Convert.localDateToString(aluguel.getDtVencimento())
							.substring(5, Convert.localDateToString(aluguel.getDtVencimento()).length()).toLowerCase()
							+ ".",
					57, 237, 11);

			addTextData(writer, "ASS: ", 60, 185, 13);
			addTextData(writer, "Data " + Convert.localDateToString(DATE_NOW), 346, 150, 12);

			document.close();

		} catch (DocumentException ex) {
			Logger.getLogger(ContratoService.class.getName()).log(Level.SEVERE, null, ex);
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	static void addTextData(PdfWriter pdfWriter, String text, int x, int y, int size) {

		PdfContentByte cb = pdfWriter.getDirectContent();
		BaseFont bf;

		try {
			bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			cb.saveState();
			cb.beginText();
			cb.moveText(x, y);
			cb.setFontAndSize(bf, size);
			cb.showText(text);
			cb.endText();
			cb.restoreState();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		pdfWriter.setPageEvent(new PDFBackground());
	}

	private static void addMetaData(Document document) {
		document.addTitle("Recibo Aluguel");
		document.addSubject("Sistema LocaFácil");
		document.addAuthor("Gotech Souções Corpotativas em TI");
	}
}
