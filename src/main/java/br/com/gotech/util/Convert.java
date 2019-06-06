package br.com.gotech.util;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import br.com.caelum.stella.inwords.InteiroSemFormato;
import br.com.caelum.stella.inwords.NumericToWordsConverter;

public class Convert {

	public static String intPorExtenso(int num) {
		NumericToWordsConverter converter;
		converter = new NumericToWordsConverter(new InteiroSemFormato());
		return converter.toWords(num);
	}

	public static String floatPorExtenso(Float num) {
		NumericToWordsConverter converter;
		converter = new NumericToWordsConverter(new InteiroSemFormato());
		return converter.toWords(num);
	}

	public static String localDateToString(LocalDate date) {		
		DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MMMM/yyyy", new Locale("pt", "BR"));
		String saida = (date).format(f).replaceAll("/", " de ");
		return saida;		
	}

	public static String floatToCurrency(Float valor) {
		Locale ptBr = new Locale("pt", "BR");
		String valorString = NumberFormat.getCurrencyInstance(ptBr).format(valor);
		return valorString;
	}
}
