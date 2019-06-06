package br.com.gotech.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.gotech.dto.DespesaImovelNewDTO;
import br.com.gotech.event.RecursoCriadoEvent;
import br.com.gotech.model.DespesaImovel;
import br.com.gotech.repository.DespesaImovelRepository;
import br.com.gotech.service.DespesaImovelService;

@RestController
@RequestMapping("/despesas_imovel")
public class DespesaImovelResource {

	@Autowired
	private DespesaImovelRepository repository;

	@Autowired
	private DespesaImovelService service;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_DESPESA_IMOVEL') and #oauth2.hasScope('read')")
	public ResponseEntity<DespesaImovel> getById(@PathVariable Long id) {
		Optional<DespesaImovel> objSave = repository.findById(id);
		return objSave.isPresent() ? ResponseEntity.ok(objSave.get()) : ResponseEntity.notFound().build();
	}

	@GetMapping("/imovel/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_DESPESA_IMOVEL') and #oauth2.hasScope('read')")
	public List<DespesaImovel> getByIdImovel(@PathVariable Long id) {
		return repository.findByImovelID(id);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_DESPESA_IMOVEL') and #oauth2.hasScope('write')")
	public ResponseEntity<DespesaImovel> create(@Valid @RequestBody DespesaImovelNewDTO obj, HttpServletResponse response) {
		DespesaImovel objSave = service.save(obj);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, objSave.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(objSave);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_DESPESA_IMOVEL') and #oauth2.hasScope('write')")
	public void delete(@PathVariable Long id) {
		repository.deleteById(id);
	}

}
