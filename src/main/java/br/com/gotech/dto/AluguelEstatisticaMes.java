package br.com.gotech.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.gotech.model.MesEnum;
import lombok.Data;

@Data
public class AluguelEstatisticaMes {

	@JsonIgnore
	private int mes;
	private String nomeMes;
	private Double total;

	public AluguelEstatisticaMes(int mes, Double total) {
		super();
		this.mes = mes;
		this.nomeMes = MesEnum.toEnum(mes).getDescricao();
		this.total = total;
	}
}
