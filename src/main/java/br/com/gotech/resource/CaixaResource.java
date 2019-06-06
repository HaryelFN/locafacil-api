package br.com.gotech.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gotech.dto.IRendimentosDTO;
import br.com.gotech.dto.RendimentoDTO;
import br.com.gotech.repository.CaixaRepository;

@RestController
@RequestMapping("/caixa")
public class CaixaResource {

	@Autowired
	private CaixaRepository repository;

	@GetMapping("/total-mes-receita")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_ALUGUEL') and #oauth2.hasScope('read')")
	public List<RendimentoDTO> getByTotalMesDespesa() {

		List<RendimentoDTO> rendimentos = new ArrayList<>();

		List<IRendimentosDTO> list = repository.getTotalRendimentosMes();

		if (!list.isEmpty()) {
			list.forEach(r -> {
				RendimentoDTO dto = new RendimentoDTO(r.getMes(), r.getTotal());
				rendimentos.add(dto);
			});
		}

		System.out.println(rendimentos);

		return rendimentos;
	}
}
