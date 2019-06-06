package br.com.gotech.repository.aluguel;

import java.util.List;

import br.com.gotech.dto.AluguelEstatisticaMes;

public interface AluguelRepositoryQuery {

	public List<AluguelEstatisticaMes> getTotalAluguelMes();
}
