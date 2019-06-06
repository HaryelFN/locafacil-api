package br.com.gotech.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mask {

	public static String cpfCnpj(String num) {
		String r = "";
		if (num.length() <= 11) {
			r = cpf(num);
		}

		if (num.length() > 11) {
			r = cnpj(num);
		}
		return r;
	}

	public static String cpf(String cpf) {
		Pattern pattern = Pattern.compile("(\\d{3})(\\d{3})(\\d{3})(\\d{2})");
		Matcher matcher = pattern.matcher(cpf);
		if (matcher.find()) {
			return matcher.replaceAll("$1.$2.$3-$4");
		}
		return cpf;
	}

	public static String cnpj(String cnpj) {
		Pattern pattern = Pattern.compile("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})");
		Matcher matcher = pattern.matcher(cnpj);
		if (matcher.find()) {
			return matcher.replaceAll("$1.$2.$3/$4-$5");
		}
		return cnpj;
	}
}
