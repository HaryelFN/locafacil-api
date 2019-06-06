package br.com.gotech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.gotech.dto.IRendimentosDTO;
import br.com.gotech.model.Caixa;

public interface CaixaRepository extends JpaRepository<Caixa, Long> {

	@Query(value = "SELECT MONTH(c.data_operacao) AS mes, SUM(c.valor) AS total FROM caixa c WHERE c.operacao = 'Receita' AND YEAR(c.data_operacao) = YEAR(CURDATE()) GROUP BY YEAR(c.data_operacao), MONTH(c.data_operacao);", nativeQuery = true)
	public List<IRendimentosDTO> getTotalRendimentosMes();
}
