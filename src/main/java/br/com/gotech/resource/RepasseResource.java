package br.com.gotech.resource;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gotech.model.Repasse;
import br.com.gotech.repository.RepasseRepository;

@RestController
@RequestMapping("/repasses")
public class RepasseResource {

	@Autowired
	private RepasseRepository repository;

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_REPASSE') and #oauth2.hasScope('read')")
	public List<Repasse> getAll() {
		return repository.getAllSort();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_REPASSE') and #oauth2.hasScope('read')")
	public ResponseEntity<Repasse> getById(@PathVariable Long id) {
		Optional<Repasse> objSave = repository.findById(id);
		return objSave.isPresent() ? ResponseEntity.ok(objSave.get()) : ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_REPASSE') and #oauth2.hasScope('write')")
	public ResponseEntity<Repasse> update(@PathVariable Long id, @Valid @RequestBody Repasse obj) {
		Repasse objSave = repository.save(obj);
		return ResponseEntity.ok(objSave);
	}
}
