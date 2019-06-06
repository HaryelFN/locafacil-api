package br.com.gotech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.gotech.model.Procurador;

@Repository
public interface ProcuradorRepository extends JpaRepository<Procurador, Long> {

}
