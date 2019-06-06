package br.com.gotech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gotech.model.Imovel;

public interface ImovelRepository extends JpaRepository<Imovel, Long> {

	public List<Imovel> findBySituacao(String situacao);
}
