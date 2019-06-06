package br.com.gotech.dto;

import java.time.LocalDate;

public interface DespesaImovelDTO {

	Long getId();

	LocalDate getDataInicio();

	LocalDate getDataFim();

	Float getValorAluguel();

	String getSituacao();
}
