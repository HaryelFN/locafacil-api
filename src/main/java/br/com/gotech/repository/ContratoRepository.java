package br.com.gotech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.gotech.dto.DespesaImovelDTO;
import br.com.gotech.model.Contrato;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

	@Query(value = "SELECT * FROM contrato c WHERE c.locatario_nome = :nome", nativeQuery = true)
	public List<Contrato> findByIdLocatario(@Param("nome") String nome);

	@Query(value = "SELECT hc.id, hc.data_inicio AS dataInicio, hc.data_fim AS dataFim, hc.valor_aluguel AS valorAluguel, hc.situacao FROM historico_contrato hc WHERE hc.id_imovel = :id ORDER BY hc.data_inicio DESC;", nativeQuery = true)
	public List<DespesaImovelDTO> findByIdImovel(@Param("id") Long id);

	@Query(value = "SELECT * FROM contrato c WHERE c.data_fim < CURDATE();", nativeQuery = true)
	public List<Contrato> findContratoVencido();

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO contrato c (c.data_inicio, c.data_fim, c.duracao, c.dia_vencimento, c.valor_aluguel, c.caucao, c.obs, c.situacao, id_imovel, id_locador, id_procurador, id_locatario, id_fiador) "
			+ "VALUES (':c.dataInicio', ':c.dataFim', :c.duracao, ':c.diaVencimento', :c.valorAluguel, :c.caucao, ':c.obs', ':c.situacao', :c.imovel.id, :c.locador.id, :c.procurador.id, :c.locatario.id, :c.fiador.id);", nativeQuery = true)
	public int insert(@Param("c") Contrato c);

	@Query(value = "SELECT MAX(c.id) FROM contrato c;", nativeQuery = true)
	public Long maxId();
}
