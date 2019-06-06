package br.com.gotech.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.gotech.dto.View;
import lombok.Data;

@Data
@Entity
public class Aluguel implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonView(View.ResumoAluguel.class)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonView(View.ResumoAluguel.class)
	@NotNull
	@Column(name = "data_vencimento")
	private LocalDate dtVencimento;

	@JsonView(View.ResumoAluguel.class)
	@Column(name = "data_pagamento")
	private LocalDate dtPagamento;

	@JsonView(View.ResumoAluguel.class)
	@NotNull
	private Float valor;

//	@JsonView(View.ResumoAluguel.class)
//	private Float iptu;

	@JsonView(View.ResumoAluguel.class)
	private Float desconto;

	@JsonView(View.ResumoAluguel.class)
	private Float total;
	
	@JsonView(View.ResumoAluguel.class)
	private String caucao;

	@JsonView(View.ResumoAluguel.class)
	@ManyToOne
	@JoinColumn(name = "id_contrato")
	private Contrato contrato;

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
		Aluguel other = (Aluguel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
