package br.com.gotech.docs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import br.com.gotech.model.Contrato;
import br.com.gotech.service.ContratoService;
import br.com.gotech.util.Convert;
import br.com.gotech.util.Mask;

public class GerarContrato {

	private static float DESCONTO = 10;

	private static int CONT_CLAUSA = 0;
	private static LocalDate DATE_NOW = LocalDate.now();

	private static Font FONT_TITULE = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	private static Font FONT_TEXT = new Font(Font.FontFamily.TIMES_ROMAN, 12);
	private static Font FONT_NUMBER = new Font(Font.FontFamily.TIMES_ROMAN, 11);

	public static ByteArrayInputStream gerarContrato(Contrato contrato) {

		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {

			PdfWriter writer = PdfWriter.getInstance(document, out);

			writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));

			HeaderFooter event = new HeaderFooter();
			writer.setPageEvent(event);

			document.open();
			addMetaData(document);
			addTextPage(document, contrato);

			document.close();

		} catch (DocumentException ex) {
			Logger.getLogger(ContratoService.class.getName()).log(Level.SEVERE, null, ex);
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	private static void addTextPage(Document document, Contrato c) throws DocumentException {

		// TITULO
		PdfPTable table = new PdfPTable(1);
		table.setSpacingBefore(5);
		table.getDefaultCell().setPadding(1);

		PdfPCell cell = new PdfPCell(new Phrase("CONTRATO DE LOCAÇÃO DE IMÓVEL PARA FINS RESIDENCIAIS", FONT_TITULE));
		cell.setPadding(6);
		table.addCell(cell);
		document.add(table);

		Paragraph linha = new Paragraph("");
		addEmptyLine(linha, 2);
		document.add(linha);

		// LOCADOR
		Paragraph locador = new Paragraph("LOCADOR: " + c.getLocador().getNome().toUpperCase() + ", "
				+ c.getLocador().getNacionalidade().toLowerCase() + ", "
				+ (c.getLocador().getSexo().equals("M")
						? c.getLocador().getEstadoCivil().substring(0, c.getLocador().getEstadoCivil().length() - 3)
						: c.getLocador().getEstadoCivil().substring(0, c.getLocador().getEstadoCivil().length() - 4)
								+ "a").toLowerCase()
				+ ", " + c.getLocador().getProfissao().toLowerCase() + ", "
				+ (c.getLocador().getSexo().equals("M") ? "portador" : "portadora") + " da cédula de identidade n° "
				+ c.getLocador().getRg() + " e do " + (c.getLocador().getCpf().length() > 11 ? "CNPJ " : "CPF ")
				+ Mask.cpfCnpj(c.getLocador().getCpf()) + ", residente e "
				+ (c.getLocador().getSexo().equals("M") ? "domiciliado" : "domiciliada") + " na cidade de "
				+ c.getLocador().getCidade() + " - " + c.getLocador().getUf() + ".", FONT_TEXT);
		locador.setAlignment(Element.ALIGN_JUSTIFIED);
		addEmptyLine(locador, 1);
		document.add(locador);

		// PROCURADOR
		if (c.getProcurador() != null) {
			Paragraph procurador = new Paragraph("LOCADOR/PROCURADOR: " + c.getProcurador().getNome().toUpperCase()
					+ ", " + c.getProcurador().getNacionalidade().toLowerCase() + ", "
					+ (c.getProcurador().getSexo().equals("M")
							? c.getProcurador().getEstadoCivil().substring(0,
									c.getProcurador().getEstadoCivil().length() - 3)
							: c.getProcurador().getEstadoCivil().substring(0,
									c.getProcurador().getEstadoCivil().length() - 4) + "a").toLowerCase()
					+ ", " + c.getProcurador().getProfissao().toLowerCase() + ""
					+ (c.getProcurador().getCreci().isEmpty() ? ""
							: (c.getProcurador().getSexo().equals("M")
									? ", inscrito no CRECI GO N° " + c.getProcurador().getCreci()
									: ", inscrita no CRECI GO N° " + c.getProcurador().getCreci()))
					+ (c.getProcurador().getSexo().equals("M") ? ", portador" : ", portadora")
					+ " da cédula de identidade n° " + c.getProcurador().getRg() + " e do "
					+ (c.getProcurador().getCpf().length() > 11 ? "CNPJ " : "CPF ")
					+ Mask.cpfCnpj(c.getProcurador().getCpf()) + ", residente e "
					+ (c.getProcurador().getSexo().equals("M") ? "domiciliado" : "domiciliada") + " na cidade de "
					+ c.getProcurador().getCidade() + " - " + c.getProcurador().getUf() + ".", FONT_TEXT);
			procurador.setAlignment(Element.ALIGN_JUSTIFIED);
			addEmptyLine(procurador, 1);
			document.add(procurador);
		}

		// LOCATARIO

		if (c.getLocatario2() != null) {
			Paragraph locatario = new Paragraph("LOCATÁRIOS: " + c.getLocatario1().getNome().toUpperCase() + ", "
					+ c.getLocatario1().getNacionalidade().toLowerCase() + ", "
					+ (c.getLocatario1().getSexo().equals("M")
							? c.getLocatario1().getEstadoCivil().substring(0,
									c.getLocatario1().getEstadoCivil().length() - 3)
							: c.getLocatario1().getEstadoCivil().substring(0,
									c.getLocatario1().getEstadoCivil().length() - 4) + "a").toLowerCase()
					+ ", " + c.getLocatario1().getProfissao().toLowerCase() + ", "
					+ (c.getLocatario1().getSexo().equals("M") ? "portador" : "portadora")
					+ " da cédula de identidade n° " + c.getLocatario1().getRg() + " e do "
					+ (c.getLocatario1().getCpf().length() > 11 ? "CNPJ " : "CPF ")
					+ Mask.cpfCnpj(c.getLocatario1().getCpf()) + " e " + c.getLocatario2().getNome().toUpperCase()
					+ ", " + c.getLocatario2().getNacionalidade().toLowerCase() + ", "
					+ (c.getLocatario2().getSexo().equals("M")
							? c.getLocatario2().getEstadoCivil().substring(0,
									c.getLocatario2().getEstadoCivil().length() - 3)
							: c.getLocatario2().getEstadoCivil().substring(0,
									c.getLocatario2().getEstadoCivil().length() - 4) + "a").toLowerCase()
					+ ", " + c.getLocatario2().getProfissao().toLowerCase() + ", "
					+ (c.getLocatario2().getSexo().equals("M") ? "portador" : "portadora")
					+ " da cédula de identidade n° " + c.getLocatario2().getRg() + " e do "
					+ (c.getLocatario2().getCpf().length() > 11 ? "CNPJ " : "CPF ")
					+ Mask.cpfCnpj(c.getLocatario2().getCpf())
					+ ", residentes e domiciliados até esta data nesta cidade de " + c.getImovel().getCidade() + " - "
					+ c.getImovel().getUf() + ".", FONT_TEXT);
			locatario.setAlignment(Element.ALIGN_JUSTIFIED);
			addEmptyLine(locatario, 1);
			document.add(locatario);
		} else {
			Paragraph locatario = new Paragraph("LOCATÁRIO: " + c.getLocatario1().getNome().toUpperCase() + ", "
					+ c.getLocatario1().getNacionalidade().toLowerCase() + ", "
					+ (c.getLocatario1().getSexo().equals("M")
							? c.getLocatario1().getEstadoCivil().substring(0,
									c.getLocatario1().getEstadoCivil().length() - 3)
							: c.getLocatario1().getEstadoCivil().substring(0,
									c.getLocatario1().getEstadoCivil().length() - 4) + "a").toLowerCase()
					+ ", " + c.getLocatario1().getProfissao().toLowerCase() + ", "
					+ (c.getLocatario1().getSexo().equals("M") ? "portador" : "portadora")
					+ " da cédula de identidade n° " + c.getLocatario1().getRg() + " e do "
					+ (c.getLocatario1().getCpf().length() > 11 ? "CNPJ " : "CPF ")
					+ Mask.cpfCnpj(c.getLocatario1().getCpf()) + ", residente e "
					+ (c.getLocatario1().getSexo().equals("M") ? "domiciliado" : "domiciliada")
					+ " até esta data nesta cidade de " + c.getImovel().getCidade() + " - " + c.getImovel().getUf()
					+ ".", FONT_TEXT);
			locatario.setAlignment(Element.ALIGN_JUSTIFIED);
			addEmptyLine(locatario, 1);
			document.add(locatario);
		}

		// IMOVEL
		String auxImovel1, auxImovel2, auxImovel3 = "";

		if (c.getImovel().getTipoImovel().equals("Casa") || c.getImovel().getTipoImovel().equals("Kitnet")) {
			auxImovel1 = "Uma";
			auxImovel2 = "DA";
			auxImovel3 = "Situada na";
		} else {
			auxImovel1 = "Um";
			auxImovel2 = "DO";
			auxImovel3 = "Situado na";
		}

		Paragraph imovel = new Paragraph("OBJETO DE LOCAÇÃO: " + auxImovel1 + " "
				+ c.getImovel().getTipoImovel().toLowerCase() + " contendo "
				+ (c.getImovel().getQtdQuarto() <= 0 ? ""
						: (c.getImovel().getQtdQuarto() > 1 ? +c.getImovel().getQtdQuarto() + " quartos, "
								: c.getImovel().getQtdQuarto() + " quarto, "))
				+ (c.getImovel().getQtdSuite() <= 0 ? ""
						: "sendo " + (c.getImovel().getQtdSuite() > 1 ? +c.getImovel().getQtdSuite() + " suites, "
								: c.getImovel().getQtdSuite() + " suite, "))
				+ (c.getImovel().getQtdSala() <= 0 ? ""
						: (c.getImovel().getQtdSala() > 1 ? +c.getImovel().getQtdSala() + " salas, "
								: c.getImovel().getQtdSala() + " sala, "))
				+ (c.getImovel().getQtdBanheiro() <= 0 ? ""
						: (c.getImovel().getQtdBanheiro() > 1 ? +c.getImovel().getQtdBanheiro() + " banheiros, "
								: c.getImovel().getQtdBanheiro() + " banheiro, "))
				+ (c.getImovel().getQtdGaragem() <= 0 ? ""
						: (c.getImovel().getQtdGaragem() > 1 ? +c.getImovel().getQtdGaragem() + " garagems, "
								: c.getImovel().getQtdGaragem() + " garagem, "))
				+ auxImovel3 + " " + c.getImovel().getLogradouro() + " - "
				+ (c.getImovel().getNumero() == null || c.getImovel().getNumero().isEmpty() ? ""
						: "n° " + c.getImovel().getNumero() + " - ")
				+ c.getImovel().getBairro() + " - " + c.getImovel().getCidade() + " - " + c.getImovel().getUf(),
				FONT_TEXT);
		imovel.setAlignment(Element.ALIGN_JUSTIFIED);
		addEmptyLine(imovel, 1);
		document.add(imovel);

		// ACESSORIOS
		if (!c.getImovel().getAcessorios().isEmpty()) {
			Paragraph acesorios = new Paragraph("ASCESSORIOS " + auxImovel2 + " "
					+ c.getImovel().getTipoImovel().toUpperCase() + ": " + c.getImovel().getAcessorios(), FONT_TEXT);
			acesorios.setAlignment(Element.ALIGN_JUSTIFIED);
			addEmptyLine(acesorios, 1);
			document.add(acesorios);
		}

		// CLAUSA 1
		Paragraph c1 = new Paragraph(
				"CLÁUSULA 1ª - O prazo da locação é de " + c.getDuracao() + " (" + Convert.intPorExtenso(c.getDuracao())
						+ ") meses, tendo por termo inicial o dia " + Convert.localDateToString(c.getDataInicio())
						+ ". E término em " + Convert.localDateToString(c.getDataFim()) + ". ",
				FONT_TEXT);
		c1.setAlignment(Element.ALIGN_JUSTIFIED);
		c1.setKeepTogether(true);
		addEmptyLine(c1, 1);
		document.add(c1);

		// CLAUSA 2
		Paragraph c2 = new Paragraph(" CLÁUSULA 2ª - O aluguel será mensal, com a data de vencimento no dia "
				+ c.getDiaVencimento().getDayOfMonth() + ", importando em "
				+ Convert.floatToCurrency(c.getValorAluguel()) + " (" + Convert.floatPorExtenso(c.getValorAluguel())
				+ " reais), reajustável com base de cálculo e índice estipulados pelo IGPM (índice geral de preços e mercado), A cada renovação de contrato. Sendo este valor pago até o dia "
				+ (c.getDataInicio().getDayOfMonth() + 1) + " de cada mês vincendo terá desconto de "
				+ Convert.floatToCurrency(descontoAluguel(c.getValorAluguel())) + " ("
				+ Convert.floatPorExtenso(descontoAluguel(c.getValorAluguel())) + " reais).", FONT_TEXT);
		c2.setAlignment(Element.ALIGN_JUSTIFIED);
		c2.setKeepTogether(true);
		addEmptyLine(c2, 1);
		document.add(c2);

		// CLAUSA 3
		Paragraph c3 = new Paragraph("CLÁUSULA 3ª - "
				+ (c.getLocatario2() == null ? "O LOCATÁRIO se compromete" : "Os LOCATÁRIOS se comprometem")
				+ " a pagar cada aluguel até o dia " + (c.getDataInicio().getDayOfMonth() + 1)
				+ " de cada mês vencido, ficando sujeito ao pagamento do valor total do aluguel sem desconto, se não efetuado o pagamento na data aprazada nesta cláusula, observando-se ainda os termos transcritos da cláusula 21ª;",
				FONT_TEXT);
		c3.setAlignment(Element.ALIGN_JUSTIFIED);
		c3.setKeepTogether(true);
		addEmptyLine(c3, 1);
		document.add(c3);

		// CLAUSA 4
		Paragraph c4 = new Paragraph(" CLÁUSULA 4ª - "
				+ (c.getLocatario2() == null ? "O LOCATÁRIO se compromete" : "Os LOCATÁRIOS se comprometem")
				+ " a solicitar a transferência de todos os serviços essenciais para seu nome ou requisitar a instalação, caso não se encontre, junto às empresas fornecedoras, devendo suportar todas as despesas decorrentes da locação, quais sejam, consumo de água, luz, telefone, gás, constituem obrigação "
				+ (c.getLocatario2() == null ? "do LOCATÁRIO " : "dos LOCATÁRIOS ")
				+ ", sendo obrigatório e saldadas nos prazos respectivos, sob pena dos acréscimos respectivos;",
				FONT_TEXT);
		c4.setAlignment(Element.ALIGN_JUSTIFIED);
		c4.setKeepTogether(true);
		addEmptyLine(c4, 1);
		document.add(c4);

		// CLAUSA 5
		if (c.getAgregarIptu().equals("V")) {
			CONT_CLAUSA = 5;
			Paragraph c5 = new Paragraph("CLÁUSULA " + CONT_CLAUSA + "ª - "
					+ (c.getLocatario2() == null ? "O LOCATÁRIO se obriga" : "Os LOCATÁRIOS se obrigam")
					+ " a pagar o imposto sobre a propriedade predial e territorial urbana e as taxas municipais incidentes sobre o imóvel na vigência deste contrato, será calculado mensalmente.",
					FONT_TEXT);
			c5.setAlignment(Element.ALIGN_JUSTIFIED);
			c5.setKeepTogether(true);
			addEmptyLine(c5, 1);
			document.add(c5);
		} else {
			CONT_CLAUSA = 4;
		}

		// CLAUSA 6
		Paragraph c6 = new Paragraph("CLÁUSULA " + (CONT_CLAUSA += 1) + "ª - "
				+ (c.getLocatario2() == null ? "O LOCATÁRIO se compromete" : "Os LOCATÁRIOS se comprometem")
				+ " a conservar o imóvel em boas condições de higiene e conservação, zelando pelas instalações hidráulicas e elétricas, enquanto perdurar a locação, restituindo o imóvel no estado em que o recebeu, com as mesmas condições determinadas no laudo de vistoria inicial, que fica fazendo parte integrante deste contrato, bem como ao pagamento dos aluguéis e encargos da locação, até efetiva entrega das chaves á administradora e assinatura do distrato.",
				FONT_TEXT);
		c6.setAlignment(Element.ALIGN_JUSTIFIED);
		c6.setKeepTogether(true);
		addEmptyLine(c6, 1);
		document.add(c6);

		// PARAGRAFO 1
		Paragraph p1 = new Paragraph("PARÁGRAFO PRIMEIRO – "
				+ (c.getLocatario2() == null ? "O LOCATÁRIO declara" : "Os LOCATÁRIOS declaram")
				+ " estar recebendo, como de fato recebe, no ato da assinatura deste contrato, o imóvel com todas as dependências em condições de serem ocupadas, conforme o laudo de vistoria inicial em anexo, que passa a integrar o presente instrumento, comprometendo-se a restituí-lo nas mesmas condições em que o recebe, procedendo aos consertos e reparos dos danos que ocorrem durante a locação, sendo lhe facultado pintar o imóvel quando lhe prover, observada a mesma cor e qualidade do material empregado, por sua conta a sem direito a qualquer indenização.",
				FONT_TEXT);
		p1.setAlignment(Element.ALIGN_JUSTIFIED);
		p1.setKeepTogether(true);
		addEmptyLine(p1, 1);
		document.add(p1);

		// PARAGRAFO 2
		Paragraph p2 = new Paragraph(
				"PARÁGRAFO SEGUNDO – Fica convencionado que qualquer discordância quanto ao relatório de vistoria do atual estado do imóvel, citado no paragrafo anterior, deverá ser feita á administradora, por escrito, no prazo de 7 (sete) dias corridos, a partir da data do inicio do contrato. Após este prazo considerar-se á aceita sem qualquer restrição.",
				FONT_TEXT);
		p2.setAlignment(Element.ALIGN_JUSTIFIED);
		p2.setKeepTogether(true);
		addEmptyLine(p2, 1);
		document.add(p2);

		// PARAGRAFO 3
		Paragraph p3 = new Paragraph("PARÁGRAFO TERCEIRO – Caso "
				+ (c.getLocatario2() == null ? "o locatário não promova" : "os locatários não promovam")
				+ " no término da locação, a pintura e os reparos necessários, poderá a Administradora, promover tais serviços, apresentando o total das despesas "
				+ (c.getLocatario2() == null ? "ao locatário" : "aos locatários")
				+ " ou a seu representante legal, para o pagamento imediato, destacando que, após o recebimento das chaves, será apresentado "
				+ (c.getLocatario2() == null ? "ao locatário" : "aos locatários")
				+ " orçamento com valor necessário a reparação do imóvel, ficando este orçamento incorporado ao débito "
				+ (c.getLocatario2() == null ? "do locatário" : "dos locatários") + ".", FONT_TEXT);
		p3.setAlignment(Element.ALIGN_JUSTIFIED);
		p3.setKeepTogether(true);
		addEmptyLine(p3, 1);
		document.add(p3);

		// PARAGRAFO 4
		Paragraph p4 = new Paragraph(
				"PARÁGRAFO QUARTO – Visando manter o padrão de qualidade do imóvel ora locado, fica desde já, acertado entre as partes, que todos os serviços de reparos necessários ao imóvel, somente poderão ser realizados por profissionais competentes, dispensando assim serviço de leigos, ficando "
						+ (c.getLocatario2() == null ? "o locatário" : "os locatários")
						+ " na obrigação de repará-los na devolução do imóvel.",
				FONT_TEXT);
		p4.setAlignment(Element.ALIGN_JUSTIFIED);
		p4.setKeepTogether(true);
		addEmptyLine(p4, 1);
		document.add(p4);

		// PARAGRAFO 5
		Paragraph p5 = new Paragraph(
				"PARÁGRAFO QUINTO - O locador não se responsabilizará por danos causados ao bom funcionamento do portão eletrônico, cerca elétrica, caixas de descarga, esgoto entupido, vaso entupido, vazamento de torneiras e registros, fossa cheia, tendo todos esses itens 15 dias de garantia de seu bom funcionamento, não apresentando nenhum defeito, serão considerados em perfeito estado, e se sofrerem algum desgaste serão de responsabilidade "
						+ (c.getLocatario2() == null ? "do locatário que deverá" : "dos locatários que deverão")
						+ ", restituí-los assim como encontrou.",
				FONT_TEXT);
		p5.setAlignment(Element.ALIGN_JUSTIFIED);
		p5.setKeepTogether(true);
		addEmptyLine(p5, 1);
		document.add(p5);

		// CLAUSA 7
		Paragraph c7 = new Paragraph("CLÁUSULA " + (CONT_CLAUSA += 1) + "ª - "
				+ (c.getLocatario2() == null ? "O LOCATÁRIO se obriga" : "Os LOCATÁRIOS se obrigam")
				+ " a permitir que o LOCADOR vistorie o imóvel, sempre que este o entenda necessário, desde que haja prévia comunicação, sempre com ressalva de eventual abuso de direito por parte do LOCADOR;",
				FONT_TEXT);
		c7.setAlignment(Element.ALIGN_JUSTIFIED);
		c7.setKeepTogether(true);
		addEmptyLine(c7, 1);
		document.add(c7);

		// CLAUSA 8
		Paragraph c8 = new Paragraph("CLÁUSULA " + (CONT_CLAUSA += 1) + "ª - "
				+ (c.getLocatario2() == null ? "O LOCATÁRIO se obriga" : "Os LOCATÁRIOS se obrigam")
				+ " a comunicar ao LOCADOR sua intenção de promover, no imóvel, modificações e benfeitorias, as quais, se aprovadas, passarão a integrar, de pleno direito, o imóvel, sem direito a qualquer ressarcimento (súm.335 STJ), ressalvando os termos da cláusula 10ª;",
				FONT_TEXT);
		c8.setAlignment(Element.ALIGN_JUSTIFIED);
		c8.setKeepTogether(true);
		addEmptyLine(c8, 1);
		document.add(c8);

		// CLAUSA 9
		Paragraph c9 = new Paragraph("CLÁUSULA " + (CONT_CLAUSA += 1) + "ª - "
				+ (c.getLocatario2() == null ? "O LOCATÁRIO se obriga" : "Os LOCATÁRIOS se obrigam")
				+ " a fazer, às suas expensas, as reparações para ingresso e permanência no imóvel, assim como de eventuais danos a que vier causar ao imóvel, com ressalva das deteriorações oriundas do uso normal do prédio;",
				FONT_TEXT);
		c9.setAlignment(Element.ALIGN_JUSTIFIED);
		c9.setKeepTogether(true);
		addEmptyLine(c9, 1);
		document.add(c9);

		// CLAUSA 10
		Paragraph c10 = new Paragraph("CLÁUSULA " + (CONT_CLAUSA += 1) + "ª - O LOCADOR se compromete a entregar, "
				+ (c.getLocatario2() == null ? "ao LOCATÁRIO" : "aos LOCATÁRIOS")
				+ ", o imóvel ora locado, em condições de servir ao uso a que se destina, renunciando o direito à indenização sob as benfeitorias úteis e necessárias, ressalvando as voluptuárias, devendo restituir o imóvel ao estado de uso comum, caso demonstre interesse em restituí-las. ",
				FONT_TEXT);
		c10.setAlignment(Element.ALIGN_JUSTIFIED);
		c10.setKeepTogether(true);
		addEmptyLine(c10, 1);
		document.add(c10);

		// CLAUSA 11
		Paragraph c11 = new Paragraph("CLÁUSULA " + (CONT_CLAUSA += 1) + "ª - "
				+ (c.getLocatario2() == null ? "O LOCATÁRIO se obriga" : "Os LOCATÁRIOS se obrigam")
				+ " a servir-se do imóvel única e exclusivamente para fins residenciais, constituindo infração contratual, passível de rescisão do presente contrato, a violação desta cláusula; ",
				FONT_TEXT);
		c11.setAlignment(Element.ALIGN_JUSTIFIED);
		c11.setKeepTogether(true);
		addEmptyLine(c11, 1);
		document.add(c11);

		// CLAUSA 12
		Paragraph c12 = new Paragraph("CLÁUSULA " + (CONT_CLAUSA += 1) + "ª - "
				+ (c.getLocatario2() == null ? "O LOCATÁRIO se obriga" : "Os LOCATÁRIOS se obrigam")
				+ " a comunicar, previamente, ao LOCADOR, sua intenção de sublocar o imóvel, devendo a autorização ser formulada por escrito e aprovado pelo locador.",
				FONT_TEXT);
		c12.setAlignment(Element.ALIGN_JUSTIFIED);
		c12.setKeepTogether(true);
		addEmptyLine(c12, 1);
		document.add(c12);

		// CLAUSA 13
		Paragraph c13 = new Paragraph("CLÁUSULA " + (CONT_CLAUSA += 1) + "ª - O LOCADOR se compromete a garantir, "
				+ (c.getLocatario2() == null ? "ao LOCATÁRIO" : "aos LOCATÁRIOS")
				+ ", o uso pacífico do prédio locado durante o tempo do contrato; ", FONT_TEXT);
		c13.setAlignment(Element.ALIGN_JUSTIFIED);
		c13.setKeepTogether(true);
		addEmptyLine(c13, 1);
		document.add(c13);

		// CLAUSA 14
		Paragraph c14 = new Paragraph("CLÁUSULA " + (CONT_CLAUSA += 1) + "ª - O LOCADOR se compromete a conservar, durante a vigência do contrato, a forma e o destino do prédio ora locado;",
				FONT_TEXT);
		c14.setAlignment(Element.ALIGN_JUSTIFIED);
		c14.setKeepTogether(true);
		addEmptyLine(c14, 1);
		document.add(c14);

		// CLAUSA 15
		Paragraph c15 = new Paragraph("CLÁUSULA " + (CONT_CLAUSA += 1) + "ª - O LOCADOR se compromete a fornecer, "
				+ (c.getLocatario2() == null ? "ao LOCATÁRIO" : "aos LOCATÁRIOS")
				+ ", recibo das importâncias pagas, com a discriminação do aluguel e de cada um dos encargos convencionados;",
				FONT_TEXT);
		c15.setAlignment(Element.ALIGN_JUSTIFIED);
		c15.setKeepTogether(true);
		addEmptyLine(c15, 1);
		document.add(c15);

		// CLAUSA 16
		Paragraph c16 = new Paragraph("CLÁUSULA " + (CONT_CLAUSA += 1) + "ª - Incidindo desapropriação sobre o imóvel objeto do presente contrato, ficam, LOCADOR e "
						+ (c.getLocatario2() == null ? "LOCATÁRIO" : "os LOCATÁRIOS")
						+ ", desobrigados do cumprimento de todas as cláusulas deste instrumento; ",
				FONT_TEXT);
		c16.setAlignment(Element.ALIGN_JUSTIFIED);
		c16.setKeepTogether(true);
		addEmptyLine(c16, 1);
		document.add(c16);
		;

		// CLAUSA 17
		Paragraph c17 = new Paragraph("CLÁUSULA " + (CONT_CLAUSA += 1) + "ª - O presente contrato não se prorrogará automaticamente, tendo por obrigação das partes, a renovação ou aviso de interrupção.",
				FONT_TEXT);
		c17.setAlignment(Element.ALIGN_JUSTIFIED);
		c17.setKeepTogether(true);
		addEmptyLine(c17, 1);
		document.add(c17);

		// AVALISTA
		if (c.getFiador() != null) {
			Paragraph avalista = new Paragraph("CLÁUSULA " + (CONT_CLAUSA += 1) + "ª – DA GARANTIA LOCATÍCIA\r\n"
					+ "Assina também o presente contrato na qualidade de FIADOR e principal pagador o Sr(a). "
					+ c.getFiador().getNome().toUpperCase() + ", " + c.getFiador().getNacionalidade().toLowerCase()
					+ ", " + (c.getFiador().getSexo().equals(
							"M") ? c.getFiador().getEstadoCivil().substring(0, c.getFiador().getEstadoCivil().length() - 3) : c.getFiador().getEstadoCivil().substring(0, c.getFiador().getEstadoCivil().length() - 4) + "a").toLowerCase()
					+ ", " + c.getFiador().getProfissao().toLowerCase() + ", "
					+ (c.getFiador().getSexo().equals("M") ? "portador" : "portadora") + " da cédula de identidade n° "
					+ c.getFiador().getRg() + " e do " + (c.getFiador().getCpf().length() > 11 ? "CNPJ " : "CPF ")
					+ Mask.cpfCnpj(c.getFiador().getCpf())
					+ ", que assume neste ato a responsabilidade pelo cumprimento integral das obrigações oriundas do presente contrato."
					+ "", FONT_TEXT);
			avalista.setAlignment(Element.ALIGN_JUSTIFIED);
			avalista.setKeepTogether(true);
			addEmptyLine(avalista, 1);
			document.add(avalista);
		}

		// CAUCAO
		if (c.getCaucao().equals("V")) {
			
			Float valorCaucao =  (c.getValorAluguel() - (c.getValorAluguel() * 10) / 100) * 3;
			
			Paragraph caucao = new Paragraph("CLÁUSULA " + (CONT_CLAUSA += 1) + "ª – DA GARANTIA LOCATÍCIA\r\n"
					+ (c.getLocatario2() == null ? "O LOCATÁRIO concorda" : "Os LOCATÁRIOS concordam")
					+ ", desde já, em depositar à título de fiança, a caução no valor de "
					+ Convert.floatToCurrency(valorCaucao) + " ("
					+ Convert.floatPorExtenso(valorCaucao)
					+ " reais) equivalente a 3 (três) meses de aluguel.", FONT_TEXT);
			caucao.setAlignment(Element.ALIGN_JUSTIFIED);
			caucao.setKeepTogether(true);
			addEmptyLine(caucao, 1);
			document.add(caucao);
		}

		// CLAUSA 18
		Paragraph c18 = new Paragraph("CLÁUSULA " + (CONT_CLAUSA += 1) + "ª - Haverá resolução do presente contrato, quando "
				+ (c.getLocatario2() == null ? "o LOCATÁRIO" : "os LOCATÁRIOS")
				+ " efetuar pagamento em atraso referente ao aluguel oriundo desta obrigação contratual, durante a sua vigência, e incorrendo "
				+ (c.getLocatario2() == null ? "o LOCATÁRIO" : "os LOCATÁRIOS")
				+ " em inadimplência por falta de pagamento, pelo período máximo de 1(um) mês, obrigando-se a entregar o imóvel nos termos das cláusulas supracitadas, sob pena de sofrer ação judicial por parte do LOCADOR; ",
				FONT_TEXT);
		c18.setAlignment(Element.ALIGN_JUSTIFIED);
		c18.setKeepTogether(true);
		addEmptyLine(c18, 1);
		document.add(c18);

		// CLAUSA 19
		Paragraph c19 = new Paragraph("CLÁUSULA " + (CONT_CLAUSA += 1) + "ª - Para toda e qualquer questão decorrente deste contrato, será competente o foro da situação do imóvel, independentemente do domicílio dos contratantes; ",
				FONT_TEXT);
		c19.setAlignment(Element.ALIGN_JUSTIFIED);
		c19.setKeepTogether(true);
		addEmptyLine(c19, 1);
		document.add(c19);

		// Paragrafo 20
		Paragraph c20 = new Paragraph(
				"E por estarem, assim, ajustados quanto aos termos do presente instrumento, cujo teor é de ambos conhecido, estando cientes e de acordo com todas as cláusulas e condições acima prestadas, firmam o presente instrumento, LOCADOR e "
						+ (c.getLocatario2() == null ? "LOCATÁRIO" : "LOCATÁRIOS")
						+ ", seus herdeiros e sucessores, nas suas 2 (duas) vias para os efeitos a que se destina, na presença de duas testemunhas idôneas, que a tudo assistiram e dão fé.",
				FONT_TEXT);
		c20.setAlignment(Element.ALIGN_JUSTIFIED);
		c20.setKeepTogether(true);
		addEmptyLine(c20, c.getLocatario2() == null ? 3 : 1);
		document.add(c20);

		// DATA
		Paragraph data = new Paragraph("Goiânia, " + Convert.localDateToString(DATE_NOW), FONT_TEXT);
		addEmptyLine(data, 2);
		document.add(data);

		if (c.getLocatario2() != null) {

			// LOCATARIO 01
			Paragraph assLocatario1 = new Paragraph("____________________________________________");
			assLocatario1.add(new Paragraph(
					"  " + c.getLocatario1().getNome().toUpperCase()
							+ (c.getLocatario1().getSexo().equals("M") ? "  (LOCATÁRIO)" : "  (LOCATÁRIA)"),
					FONT_TEXT));
			addEmptyLine(assLocatario1, 3);
			document.add(assLocatario1);

			// LOCATARIO 02
			Paragraph assLocatario2 = new Paragraph("____________________________________________");
			assLocatario2.add(new Paragraph(
					"  " + c.getLocatario2().getNome().toUpperCase()
							+ (c.getLocatario2().getSexo().equals("M") ? "  (LOCATÁRIO)" : "  (LOCATÁRIA)"),
					FONT_TEXT));
			addEmptyLine(assLocatario2, 3);
			document.add(assLocatario2);

		} else {

			// LOCATARIO
			Paragraph assLocatario = new Paragraph("____________________________________________");
			assLocatario.add(new Paragraph(
					"  " + c.getLocatario1().getNome().toUpperCase()
							+ (c.getLocatario1().getSexo().equals("M") ? "  (LOCATÁRIO)" : "  (LOCATÁRIA)"),
					FONT_TEXT));
			addEmptyLine(assLocatario, 3);
			document.add(assLocatario);
		}

		// ASSINATURA FIADOR
		if (c.getFiador() != null) {
			Paragraph assAvalista = new Paragraph("____________________________________________");
			assAvalista.add(new Paragraph("  " + c.getFiador().getNome().toUpperCase()
					+ (c.getFiador().getSexo().equals("M") ? "  (FIADOR)" : "  (FIADORA)"), FONT_TEXT));
			addEmptyLine(assAvalista, 3);
			document.add(assAvalista);
		}

		// ASSINATURA PROCURADOR
		if (c.getProcurador() != null) {
			Paragraph assProcurador = new Paragraph("____________________________________________");
			assProcurador.add(new Paragraph(
					"  " + c.getProcurador().getNome().toUpperCase()
							+ (c.getProcurador().getSexo().equals("M") ? "  (PROCURADOR)" : "  (PROCURADORA)"),
					FONT_TEXT));
			addEmptyLine(assProcurador, 3);
			document.add(assProcurador);
		} else {

			// ASSINATURA LOCADOR
			Paragraph assLocador = new Paragraph("____________________________________________");
			assLocador.add(new Paragraph("  " + c.getLocador().getNome().toUpperCase()
					+ (c.getLocador().getSexo().equals("M") ? "  (LOCADOR)" : "  (LOCADORA)"), FONT_TEXT));
			addEmptyLine(assLocador, 3);
			document.add(assLocador);
		}
	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	// ADICIONA PAGINACAO
	static class HeaderFooter extends PdfPageEventHelper {

		public void onEndPage(PdfWriter writer, Document document) {
			Rectangle rect = writer.getBoxSize("art");

			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
					new Phrase(String.valueOf(writer.getPageNumber()), FONT_NUMBER), rect.getRight() - 30,
					rect.getBottom() - 18, 0);
		}
	}

	private static void addMetaData(Document document) {
		document.addTitle("Contrato Imóvel");
		document.addSubject("Sistema LocaFácil");
		document.addAuthor("Gotech Souções Corpotativas em TI");
	}

	private static Float descontoAluguel(Float valor) {
		return (valor * DESCONTO) / 100;
	}
}