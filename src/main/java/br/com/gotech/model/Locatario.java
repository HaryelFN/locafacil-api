package br.com.gotech.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.gotech.dto.View;
import br.com.gotech.util.CpfCnpj;
import lombok.Data;

@Data
@Entity
public class Locatario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String sexo;

	@JsonView(View.ResumoContrato.class)
	@NotNull
	private String nome;

	@NotNull
	private String rg;

	@JsonView(View.ResumoContrato.class)
	@CpfCnpj
	@NotNull
	private String cpf;

	@NotNull
	private String nacionalidade;

	@NotNull
	@Column(name = "estado_civil")
	private String estadoCivil;

	@NotNull
	private String profissao;

	@NotNull
	private String telefone;

	private String email;

	private String cep;

	private String uf;

	private String cidade;

	private String bairro;

	private String logradouro;

	private String numero;

	private String complemento;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Locatario other = (Locatario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

}
