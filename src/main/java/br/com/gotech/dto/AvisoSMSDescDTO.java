package br.com.gotech.dto;

import java.time.LocalDate;

public interface AvisoSMSDescDTO {

	String getNome();

	String getTelefone();

	LocalDate getDataVencimento();

	Float getValor();

}
