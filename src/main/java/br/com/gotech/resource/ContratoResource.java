package br.com.gotech.resource;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.gotech.docs.GerarContrato;
import br.com.gotech.dto.ContratoNewDTO;
import br.com.gotech.dto.DespesaImovelDTO;
import br.com.gotech.dto.View;
import br.com.gotech.event.RecursoCriadoEvent;
import br.com.gotech.exceptionhandler.LocaFacilExceptionHandler.Erro;
import br.com.gotech.model.Contrato;
import br.com.gotech.repository.ContratoRepository;
import br.com.gotech.service.ContratoService;
import br.com.gotech.service.exception.FiadorInexistenteException;
import br.com.gotech.service.exception.ImovelInexistenteOuOcupadoException;
import br.com.gotech.service.exception.LocadorInexistenteOuOcupadoException;
import br.com.gotech.service.exception.LocatarioInexistenteOuOcupadoException;
import br.com.gotech.service.exception.ProcuradorInexistenteException;

@RestController
@RequestMapping("/contratos")
public class ContratoResource {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ContratoRepository repository;

	@Autowired
	private ContratoService service;

	@Autowired
	private ApplicationEventPublisher publisher;

	@JsonView(View.ResumoContrato.class)
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CONTRATO') and #oauth2.hasScope('read')")
	public List<Contrato> getAll() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CONTRATO') and #oauth2.hasScope('read')")
	public ResponseEntity<Contrato> getById(@PathVariable Long id) {
		Optional<Contrato> objSave = repository.findById(id);
		return objSave.isPresent() ? ResponseEntity.ok(objSave.get()) : ResponseEntity.notFound().build();
	}

	@JsonView(View.ResumoContrato.class)
	@GetMapping("/vencidos")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CONTRATO') and #oauth2.hasScope('read')")
	public List<Contrato> getVencidos() {
		return repository.findContratoVencido();
	}

	@GetMapping("/findByLocatario/{nome}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CONTRATO') and #oauth2.hasScope('read')")
	public List<Contrato> findByLocatario(@PathVariable String nome) {
		return repository.findByIdLocatario(nome);
	}

	@GetMapping("/findByIdImovel/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CONTRATO') and #oauth2.hasScope('read')")
	public List<DespesaImovelDTO> findByIdImovel(@PathVariable Long id) {
		return repository.findByIdImovel(id);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CONTRATO') and #oauth2.hasScope('write')")
	public ResponseEntity<Contrato> create(@Valid @RequestBody ContratoNewDTO obj, HttpServletResponse response) {
		Contrato objSave = service.save(obj);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, objSave.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(objSave);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CONTRATO') and #oauth2.hasScope('write')")
	public ResponseEntity<Contrato> update(@PathVariable Long id, @Valid @RequestBody Contrato obj) {
		Contrato objSave = service.update(obj);
		return ResponseEntity.ok(objSave);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_CONTRATO') and #oauth2.hasScope('write')")
	public void delete(@PathVariable Long id) {
		repository.deleteById(id);
	}

	@RequestMapping(value = "/pdf/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CONTRATO') and #oauth2.hasScope('read')")
	public ResponseEntity<InputStreamResource> getContratoPDF(@PathVariable Long id) {

		ByteArrayInputStream bis = GerarContrato.gerarContrato(service.isContrato(id));

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=contrato.pdf");

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(new InputStreamResource(bis));
	}

	@ExceptionHandler({ ImovelInexistenteOuOcupadoException.class })
	public ResponseEntity<Object> handleImovelInexistenteOuOcupadoException(ImovelInexistenteOuOcupadoException ex) {
		String mensagemUsuario = messageSource.getMessage("imovel.inexistente-ou-ocupado", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}

	@ExceptionHandler({ LocadorInexistenteOuOcupadoException.class })
	public ResponseEntity<Object> handleLocadorInexistenteOuOcupadoException(LocadorInexistenteOuOcupadoException ex) {
		String mensagemUsuario = messageSource.getMessage("locador.inexistente", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}

	@ExceptionHandler({ ProcuradorInexistenteException.class })
	public ResponseEntity<Object> handleProcuradorInexistenteOuOcupadoException(ProcuradorInexistenteException ex) {
		String mensagemUsuario = messageSource.getMessage("procurador.inexistente", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}

	@ExceptionHandler({ LocatarioInexistenteOuOcupadoException.class })
	public ResponseEntity<Object> handleLocatarioInexistenteOuOcupadoException(
			LocatarioInexistenteOuOcupadoException ex) {
		String mensagemUsuario = messageSource.getMessage("locatario.inexistente", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}

	@ExceptionHandler({ FiadorInexistenteException.class })
	public ResponseEntity<Object> handleFiadorInexistenteOuOcupadoException(FiadorInexistenteException ex) {
		String mensagemUsuario = messageSource.getMessage("fiador.inexistente", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}
}
