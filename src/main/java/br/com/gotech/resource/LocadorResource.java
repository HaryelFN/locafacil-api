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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.gotech.event.RecursoCriadoEvent;
import br.com.gotech.model.Locador;
import br.com.gotech.repository.LocadorRepository;

@RestController
@RequestMapping("/locadores")
public class LocadorResource {

	@Autowired
	private LocadorRepository repository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LOCADOR') and #oauth2.hasScope('read')")
	public List<Locador> getAll() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LOCADOR') and #oauth2.hasScope('read')")
	public ResponseEntity<Locador> getById(@PathVariable Long id) {
		Optional<Locador> objSave = repository.findById(id);
		return objSave.isPresent() ? ResponseEntity.ok(objSave.get()) : ResponseEntity.notFound().build();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LOCADOR') and #oauth2.hasScope('write')")
	public ResponseEntity<Locador> create(@Valid @RequestBody Locador obj, HttpServletResponse response) {
		Locador objSave = repository.save(obj);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, objSave.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(objSave);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LOCADOR') and #oauth2.hasScope('write')")
	public ResponseEntity<Locador> update(@PathVariable Long id, @Valid @RequestBody Locador obj) {
		Locador objSave = repository.save(obj);
		return ResponseEntity.ok(objSave);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LOCADOR') and #oauth2.hasScope('write')")
	public void delete(@PathVariable Long id) {
		repository.deleteById(id);
	}
}
