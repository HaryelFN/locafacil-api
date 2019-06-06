package br.com.gotech.model;

public enum MesEnum {

	Janeiro(1, "Janeiro"), Fevereiro(2, "Fevereiro"), Março(3, "Março"), Abril(4, "Abril"), Maio(5, "Maio"),
	Junho(6, "Junho"), Julho(7, "Julho"), Agosto(8, "Agosto"), Setembro(9, "Setembro"), Outubro(10, "Outubro"),
	Novembro(11, "Novembro"), Dezembro(12, "Dezembro");

	private int cod;
	private String descricao;

	private MesEnum(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}

	public int getCod() {
		return cod;
	}

	public String getDescricao() {
		return descricao;
	}

	public static MesEnum toEnum(Integer cod) {

		if (cod == null) {
			return null;
		}

		for (MesEnum x : MesEnum.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}

		throw new IllegalArgumentException("Id inválido: " + cod);
	}
}
