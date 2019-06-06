package br.com.gotech.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ContratoNewDTO {
	
	private Long id;
	private LocalDate dataInicio;
	private LocalDate dataFim;
	private Integer duracao;
	private LocalDate diaVencimento;
	private Float valorAluguel;
	private String agregarIptu;
	// private Float valorIptu;
	private String caucao;
	private Float valorCaucao;
	private String obs;
	private String situacao;
	
	private Long idImovel;
	private Long idLocador;
	private Long idProcurador;
	private Long idLocatario1;
	private Long idLocatario2;
	private Long idFiador;

}
