package br.com.gotech.resource;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.gotech.docs.GerarRecibo;
import br.com.gotech.dto.AluguelEstatisticaMes;
import br.com.gotech.dto.View;
import br.com.gotech.model.Aluguel;
import br.com.gotech.repository.AluguelRepository;
import br.com.gotech.service.AluguelService;
import br.com.gotech.sms.PapovaiHttpClientConnector;

@RestController
@RequestMapping("/alugueis")
public class AluguelResource {

	@Autowired
	private AluguelRepository repository;

	@Autowired
	private AluguelService service;

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_ALUGUEL') and #oauth2.hasScope('read')")
	public ResponseEntity<Aluguel> getById(@PathVariable Long id) {
		Optional<Aluguel> objSave = repository.findById(id);
		return objSave.isPresent() ? ResponseEntity.ok(objSave.get()) : ResponseEntity.notFound().build();
	}

	@JsonView(View.ResumoAluguel.class)
	@GetMapping("/findByContrato/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CONTRATO') and #oauth2.hasScope('read')")
	public List<Aluguel> getByLocatario(@PathVariable Long id) {
		return repository.findByContratoId(id);
	}

	@JsonView(View.ResumoAluguel.class)
	@GetMapping("/vencidos")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_ALUGUEL') and #oauth2.hasScope('read')")
	public List<Aluguel> getByVencidos() {
		return repository.findByVencidosForEmail();
	}

	@JsonView(View.ResumoAluguel.class)
	@GetMapping("/qtdVencidosByContrato/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_ALUGUEL') and #oauth2.hasScope('read')")
	public Integer getVencidosByIdContrato(@PathVariable Long id) {
		return repository.qtdVencidosByIDContrato(id);
	}

	@GetMapping("/estatisticas/total-mes")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_ALUGUEL') and #oauth2.hasScope('read')")
	public List<AluguelEstatisticaMes> getByTotalAluguelMes() {
		return repository.getTotalAluguelMes();
	}

	@PutMapping("/receber/{id}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_ALUGUEL') and #oauth2.hasScope('write')")
	public ResponseEntity<Aluguel> receberAluguel(@PathVariable Long id, @Valid @RequestBody Aluguel obj) {
		Aluguel objSalvo = service.atualizar(id, obj);
		return ResponseEntity.ok(objSalvo);
	}

	@RequestMapping(value = "/recibo/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_ALUGUEL') and #oauth2.hasScope('read')")
	public ResponseEntity<InputStreamResource> getRecibo(@PathVariable Long id) {

		ByteArrayInputStream bis = GerarRecibo.gerar(service.isAluguel(id));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=recibo.pdf");

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(new InputStreamResource(bis));
	}
}
