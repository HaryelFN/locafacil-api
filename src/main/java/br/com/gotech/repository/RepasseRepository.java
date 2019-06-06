package br.com.gotech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.gotech.model.Repasse;

public interface RepasseRepository extends JpaRepository<Repasse, Long> {

	@Query(value = "SELECT r.* FROM repasse r ORDER BY r.id DESC;", nativeQuery = true)
	List<Repasse> getAllSort();

}
