package br.com.gotech.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.gotech.dto.IsContrato;
import br.com.gotech.model.Locatario;

public interface LocatarioRepository extends JpaRepository<Locatario, Long>{

	@Query(value  = "SELECT i.bairro, i.logradouro, i.uf FROM locatario l INNER JOIN contrato c ON c.id_locatario_1 = l.id INNER JOIN imovel i ON i.id = c.id_imovel WHERE l.id = :id", nativeQuery = true)
	public Optional<IsContrato> isContrato(@Param("id") Long id);
}
