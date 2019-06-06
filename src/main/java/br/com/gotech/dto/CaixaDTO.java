package br.com.gotech.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.gotech.model.MesEnum;
import lombok.Data;

@Data
public class CaixaDTO {

	@JsonIgnore
	private int mes;
	private String nomeMes;
	private Double total;
	private String operacao;

	public CaixaDTO(int mes, Double total, String operacao) {
		super();
		this.mes = mes;
		this.nomeMes = MesEnum.toEnum(mes).getDescricao();
		this.total = total;
		this.operacao = operacao;
	}
}
