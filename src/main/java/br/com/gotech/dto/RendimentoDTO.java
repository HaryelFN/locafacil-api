package br.com.gotech.dto;

import br.com.gotech.model.MesEnum;
import lombok.Data;

@Data
public class RendimentoDTO {

	private int mes;
	private Double total;
	private String nomeMes;

	public RendimentoDTO(int mes, Double total) {
		super();
		this.mes = mes;
		this.total = total;
		this.nomeMes = MesEnum.toEnum(mes).getDescricao();
	}
}
