package br.com.gotech.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity(name = "historico_aluguel")
public class HistoricoContrato implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "data_inicio")
	private LocalDate dataInicio;

	@NotNull
	@Column(name = "data_fim")
	private LocalDate dataFim;

	@NotNull
	@Column(name = "valor_aluguel")
	private Float valorAluguel;

	@NotNull
	private String situacao;

	@OneToOne
	@JoinColumn(name = "id_imovel")
	private Imovel imovel;

	@OneToOne
	@JoinColumn(name = "id_locador")
	private Locador locador;

	@OneToOne(optional = true)
	@JoinColumn(name = "id_procurador", nullable = true)
	private Procurador procurador;

	@OneToOne
	@JoinColumn(name = "id_locatario_1")
	private Locatario locatario1;

	@OneToOne(optional = true)
	@JoinColumn(name = "id_locatario_2", nullable = true)
	private Locatario locatario2;

	@OneToOne(optional = true)
	@JoinColumn(name = "id_fiador", nullable = true)
	private Fiador fiador;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoricoContrato other = (HistoricoContrato) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
