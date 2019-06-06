package br.com.gotech.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DespesaImovelNewDTO {

	private LocalDate data;
	private Float valor;
	private String descricao;
	private Long idImovel;
}
