package br.com.gotech.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import br.com.gotech.dto.View;
import lombok.Data;

@Data
@Entity
@ResponseBody
public class Imovel implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonView(View.ResumoImovel.class)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonView(View.ResumoImovel.class)
	@NotNull
	private String situacao;

	@JsonView(View.ResumoImovel.class)
	@NotNull
	@Column(name = "tipo_imovel")
	private String tipoImovel;

	@Column(name = "qtd_quarto")
	private int qtdQuarto;

	@Column(name = "qtd_suite")
	private int qtdSuite;

	@Column(name = "qtd_banheiro")
	private int qtdBanheiro;

	@Column(name = "qtd_sala")
	private int qtdSala;

	@Column(name = "qtd_garagem")
	private int qtdGaragem;

	@Column(name = "area_terreno")
	private int areaTerreno;

	@Column(name = "area_construida")
	private int areaConstruida;

	@Column(name = "valor_aluguel")
	private BigDecimal valorAluguel;

	@Column(name = "valor_condominio")
	private BigDecimal valorCondominio;

	@Column(name = "valor_iptu")
	private BigDecimal valorIptu;

	private String acessorios;

	private String cep;

	@NotNull
	private String uf;

	@NotNull
	@Size(min = 3, max = 30)
	private String cidade;

	@JsonView({ View.ResumoImovel.class, View.ResumoContrato.class })
	@NotNull
	@Size(min = 3, max = 30)
	private String bairro;

	@JsonView({ View.ResumoImovel.class, View.ResumoContrato.class })
	@NotNull
	@Size(min = 3, max = 50)
	private String logradouro;

	private String numero;

	@JsonView(View.ResumoImovel.class)
	@Size(max = 100)
	private String complemento;

	@OneToMany(mappedBy = "imovel")
	private List<Foto> fotos;

	@OneToMany(mappedBy = "imovel")
	private List<DespesaImovel> despesas;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "imovel")
	private Contrato contrato;

	@NotNull
	@OneToOne
	@JoinColumn(name = "id_proprietario")
	private Locador proprietario;

	@JsonIgnore
	@Transient
	public boolean isOcupado() {
		return this.situacao.equals("ocupado") ? true : false;
	}

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
		Imovel other = (Imovel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
