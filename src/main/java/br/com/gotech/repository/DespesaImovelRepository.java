package br.com.gotech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.gotech.model.DespesaImovel;

public interface DespesaImovelRepository extends JpaRepository<DespesaImovel, Long> {

	@Query(value = "SELECT * FROM despesa_imovel di WHERE di.id_imovel = :id ORDER BY di.data_criacao DESC;", nativeQuery = true)
	List<DespesaImovel> findByImovelID(@Param("id")Long id);

}
