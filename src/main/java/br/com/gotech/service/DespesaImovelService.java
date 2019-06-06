package br.com.gotech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gotech.dto.DespesaImovelNewDTO;
import br.com.gotech.model.DespesaImovel;
import br.com.gotech.repository.DespesaImovelRepository;
import br.com.gotech.repository.ImovelRepository;

@Service
public class DespesaImovelService {

	@Autowired
	private DespesaImovelRepository repository;

	@Autowired
	private ImovelRepository imovelRepository;

	public DespesaImovel save(DespesaImovelNewDTO obj) {

		DespesaImovel despesaImovel = new DespesaImovel();

		despesaImovel.setData(obj.getData());
		despesaImovel.setValor(obj.getValor());
		despesaImovel.setDescricao(obj.getDescricao());
		despesaImovel.setImovel(imovelRepository.getOne(obj.getIdImovel()));

		return repository.save(despesaImovel);

	}
}
