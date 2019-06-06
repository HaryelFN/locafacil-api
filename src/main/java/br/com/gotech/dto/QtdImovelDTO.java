package br.com.gotech.dto;

import lombok.Data;

@Data
public class QtdImovelDTO {

	private int livre;
	private int ocupado;

	public QtdImovelDTO(int livre, int ocupado) {
		super();
		this.livre = livre;
		this.ocupado = ocupado;
	}
}
