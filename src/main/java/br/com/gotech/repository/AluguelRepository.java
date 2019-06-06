package br.com.gotech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.gotech.dto.AvisoSMSDescDTO;
import br.com.gotech.model.Aluguel;
import br.com.gotech.repository.aluguel.AluguelRepositoryQuery;

public interface AluguelRepository extends JpaRepository<Aluguel, Long>, AluguelRepositoryQuery {

	public List<Aluguel> findByContratoId(Long id);

	// Native Query
	@Query(value = "SELECT * FROM aluguel a WHERE a.data_pagamento IS NULL AND a.data_vencimento <= NOW();", nativeQuery = true)
	public List<Aluguel> findByVencidosForEmail();
	
	// Native Query
	@Query(value = "SELECT * FROM aluguel a WHERE a.data_pagamento IS NULL AND (SELECT DATEDIFF(CURDATE(), a.data_vencimento) = 2);", nativeQuery = true)
	public List<Aluguel> findByVencidosForSMS();

	// Native Query
	@Query(value = "SELECT COUNT(*) FROM aluguel a WHERE a.id_contrato = :idContrato AND a.total IS NULL;", nativeQuery = true)
	public Integer qtdVencidosByIDContrato(@Param("idContrato") Long idContrato);

	// Native Query
	@Query(value = "SELECT l.nome, l.telefone, a.data_vencimento AS dataVencimento, a.valor FROM aluguel a INNER JOIN contrato c ON c.id = a.id_contrato INNER JOIN locatario l ON l.id = c.id_locatario_1 INNER JOIN imovel i ON i.id = c.id_imovel WHERE a.data_pagamento IS NULL AND a.data_vencimento = CURDATE();", nativeQuery = true)
	public List<AvisoSMSDescDTO> getAvisoSMSDescDTO();
	
	@Query(value = "UPDATE aluguel a SET a.data_vencimento = ':obj.dtVencimento', a.data_pagamento = ':obj.dtPagamento', a.valor = :obj.valor, a.desconto = :obj.desconto, a.total = :obj.total, a.caucao = ':obj.caucao', a.id_contrato = :obj.contrato.id WHERE a.id = 0;", nativeQuery = true)
	public Aluguel updateObj(@Param("obj") Aluguel obj);
}
