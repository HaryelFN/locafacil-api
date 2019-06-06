package br.com.gotech.dto;

import lombok.Data;

@Data
public class Anexo {
	private String anexo;
	private String urlAnexo;

	public Anexo(String nome, String url) {
		super();
		this.anexo = nome;
		this.urlAnexo = url;
	}
}
