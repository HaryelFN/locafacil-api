package br.com.gotech.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import br.com.gotech.model.Fiador;
import br.com.gotech.model.Imovel;
import br.com.gotech.model.Locador;
import br.com.gotech.model.Locatario;
import br.com.gotech.model.Procurador;
import lombok.Data;

@Data
public class ContratoNovo {
	
	@NotNull
	private LocalDate dataInicio;

	@NotNull
	private LocalDate dataFim;

	@NotNull
	private Float valorAluguel;

	@NotNull
	private int diaVencimento;

	private int duracao;
	
	private String obs;
	
	@NotNull
	private Imovel imovel;

	@NotNull
	private Locatario locatario;

	@NotNull
	private Locador locador;
	
	private Procurador procurador;
	
	private Fiador avalista;

}
