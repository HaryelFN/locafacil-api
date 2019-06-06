package br.com.gotech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.gotech.model.Foto;

public interface FotoRepository extends JpaRepository<Foto, Long> {

	// Query native
	@Query(value = "SELECT * FROM foto f WHERE f.id_imovel = :id", nativeQuery = true)
	public List<Foto> findByIdImovel(@Param("id") Long id);

}
