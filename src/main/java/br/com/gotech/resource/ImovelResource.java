package br.com.gotech.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.gotech.dto.Anexo;
import br.com.gotech.dto.QtdImovelDTO;
import br.com.gotech.dto.View;
import br.com.gotech.event.RecursoCriadoEvent;
import br.com.gotech.model.Imovel;
import br.com.gotech.repository.ImovelRepository;
import br.com.gotech.service.ImovelService;
import br.com.gotech.storage.S3;

@RestController
@RequestMapping("/imoveis")
public class ImovelResource {

	@Autowired
	private ImovelRepository repository;

	@Autowired
	private ImovelService service;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private S3 s3;

	@JsonView(View.ResumoImovel.class)
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_IMOVEL') and #oauth2.hasScope('read')")
	public List<Imovel> getAll() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_IMOVEL') and #oauth2.hasScope('read')")
	public ResponseEntity<Imovel> getById(@PathVariable Long id) {
		Imovel objSave = service.FindById(id);
		return objSave != null ? ResponseEntity.ok(objSave) : ResponseEntity.notFound().build();
	}

	@GetMapping("/situacao/{situacao}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_IMOVEL') and #oauth2.hasScope('read')")
	public List<Imovel> getBySituacao(@PathVariable String situacao) {
		return repository.findBySituacao(situacao);
	}

	@GetMapping("/qtdimoveis")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_IMOVEL') and #oauth2.hasScope('read')")
	public QtdImovelDTO QtdImoveis() {
		return service.getQtdImoveis();
	}

	@PostMapping("/fotos")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_IMOVEL') and #oauth2.hasScope('write')")
	public List<Anexo> uploadAnexo(@RequestParam List<MultipartFile> anexos) throws IOException {

		List<Anexo> list = new ArrayList<>();
		anexos.forEach(a -> {

			String nome = null;

			try {
				nome = s3.salvarTemporariamente("/imoveis", a, true);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

			list.add(new Anexo(nome, s3.configurarUrl(nome, "/imoveis")));
		});
		return list;
	}

	@DeleteMapping("/fotos/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_IMOVEL') and #oauth2.hasScope('write')")
	public void removerPhotos(@PathVariable Long id) {
		service.deletarFotos(id);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_IMOVEL') and #oauth2.hasScope('write')")
	public ResponseEntity<Imovel> create(@Valid @RequestBody Imovel obj, HttpServletResponse response) {
		Imovel objSave = service.salvar(obj);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, objSave.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(objSave);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_IMOVEL') and #oauth2.hasScope('write')")
	public ResponseEntity<Imovel> update(@PathVariable Long id, @Valid @RequestBody Imovel obj) {
		Imovel objSave = service.atualizar(id, obj);
		return ResponseEntity.ok(objSave);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_IMOVEL') and #oauth2.hasScope('write')")
	public void delete(@PathVariable Long id) {
		service.deletar(id);
	}
}
