package br.com.gotech.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.gotech.dto.ContratoNewDTO;
import br.com.gotech.mail.Mailer;
import br.com.gotech.model.Contrato;
import br.com.gotech.model.Fiador;
import br.com.gotech.model.Imovel;
import br.com.gotech.model.Locador;
import br.com.gotech.model.Locatario;
import br.com.gotech.model.Procurador;
import br.com.gotech.repository.ContratoRepository;
import br.com.gotech.repository.FiadorRepository;
import br.com.gotech.repository.ImovelRepository;
import br.com.gotech.repository.LocadorRepository;
import br.com.gotech.repository.LocatarioRepository;
import br.com.gotech.repository.ProcuradorRepository;
import br.com.gotech.service.exception.FiadorInexistenteException;
import br.com.gotech.service.exception.ImovelInexistenteOuOcupadoException;
import br.com.gotech.service.exception.LocadorInexistenteOuOcupadoException;
import br.com.gotech.service.exception.LocatarioInexistenteOuOcupadoException;
import br.com.gotech.service.exception.ProcuradorInexistenteException;

@Service
public class ContratoService {

	private static final Logger logger = LoggerFactory.getLogger(ContratoService.class);

	@Autowired
	private ContratoRepository repository;

	@Autowired
	private ImovelRepository imovelRepository;

	@Autowired
	private LocadorRepository locadorRepository;

	@Autowired
	private ProcuradorRepository procuradorRepository;

	@Autowired
	private LocatarioRepository locatarioRepository;

	@Autowired
	private FiadorRepository fiadorRepository;

	@Autowired
	private Mailer mailer;

	@Scheduled(cron = "0 0 9 * * *")
	public void avisoContratoVencidoEmail() {

		if (logger.isDebugEnabled()) {
			logger.debug("Preparando envio de email-s de contratros vencidos");
		}

		List<Contrato> contratos = repository.findContratoVencido();

		if (contratos.isEmpty()) {
			logger.info("Sem contratos vencidos.");
			return;
		}

		logger.info("Existem {} contratos vencidos.", contratos.size());

		List<Procurador> procuradores = procuradorRepository.findAll();
		List<Locador> locadores = locadorRepository.findAll();

		mailer.contratoVencidos(contratos, procuradores, locadores);

		logger.info("Envio de email-s concluido.");
	}

//	@Scheduled(cron = "0 0 9 * * *")
//	public void avisoContratoVencidoSMS() {
//		if (logger.isDebugEnabled()) {
//			logger.debug("Preparando envio de SMS de contratros vencidos");
//		}
//
//		List<Contrato> contratos = repository.findContratoVencido();
//
//		if (contratos.isEmpty()) {
//			logger.info("Sem contratos vencidos.");
//			return;
//		}
//
//
//		logger.info("Existem {} contratos vencidos", contratos.size());
//
//		String numbers = null;
//		List<Procurador> procuradores = procuradorRepository.findAll();
//		List<Locador> locadores = locadorRepository.findAll();
//
//		numbers = procuradores.stream().map(e -> e.getTelefone().replaceAll("\\D", "") + ";").reduce("",
//				String::concat);
//		numbers += locadores.stream().map(e -> e.getTelefone().replaceAll("\\D", "") + ";").reduce("", String::concat);
//
//		PapovaiHttpClientConnector papovaiHttpClientConnector = new PapovaiHttpClientConnector();
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		Date date = new Date();
//
//		try {
//			papovaiHttpClientConnector.send(numbers, "Atenção existem contrato(s) . Sistema Locafacil",
//					dateFormat.format(date).toString());
//		} catch (Exception e) {
//			logger.info(e.getMessage());
//		}
//
//		logger.info("Envio de SMS concluido.");
//	}

	public Contrato save(ContratoNewDTO contrato) {

		Contrato obj = new Contrato();

		obj.setDataInicio(contrato.getDataInicio());
		obj.setDataFim(contrato.getDataFim());
		obj.setDuracao(contrato.getDuracao());
		obj.setDiaVencimento(contrato.getDiaVencimento());
		obj.setValorAluguel(contrato.getValorAluguel());
		obj.setAgregarIptu(contrato.getAgregarIptu());
		// obj.setValorIptu(contrato.getValorIptu());
		obj.setCaucao(contrato.getCaucao());
		obj.setValorCaucao(contrato.getValorCaucao());
		obj.setObs(contrato.getObs());
		obj.setSituacao(contrato.getSituacao());

		obj.setImovel(imovelRepository.getOne(contrato.getIdImovel()));
		obj.setLocador(locadorRepository.getOne(contrato.getIdLocador()));
		obj.setLocatario1(locatarioRepository.getOne(contrato.getIdLocatario1()));

		if (contrato.getIdLocatario2() == null) {
			obj.setLocatario2(null);
		} else {
			obj.setLocatario2(locatarioRepository.getOne(contrato.getIdLocatario2()));
		}

		if (contrato.getIdProcurador() == null) {
			obj.setProcurador(null);
		} else {
			obj.setProcurador(procuradorRepository.getOne(contrato.getIdProcurador()));
		}

		if (contrato.getIdFiador() == null) {
			obj.setFiador(null);
		} else {
			obj.setFiador(fiadorRepository.getOne(contrato.getIdFiador()));
		}

		return repository.save(obj);
	}

	public Contrato update(Contrato contrato) {

		Contrato objSave = isContrato(contrato.getId());

		Contrato obj = new Contrato();

		obj.setId(contrato.getId());
		obj.setDataInicio(contrato.getDataInicio());
		obj.setDataFim(contrato.getDataFim());
		obj.setDuracao(contrato.getDuracao());
		obj.setDiaVencimento(contrato.getDiaVencimento());
		obj.setValorAluguel(contrato.getValorAluguel());
		obj.setAgregarIptu(contrato.getAgregarIptu());
		obj.setCaucao(contrato.getCaucao());
		obj.setObs(contrato.getObs());
		obj.setSituacao(contrato.getSituacao());

		obj.setImovel(imovelRepository.getOne(contrato.getImovel().getId()));
		obj.setLocador(locadorRepository.getOne(contrato.getLocador().getId()));
		obj.setLocatario1(locatarioRepository.getOne(contrato.getLocatario1().getId()));

		if (contrato.getLocatario2().getId() == null) {
			obj.setLocatario2(null);
		} else {
			obj.setLocatario2(locatarioRepository.getOne(contrato.getLocatario2().getId()));
		}

		if (contrato.getProcurador().getId() == null) {
			obj.setProcurador(null);
		} else {
			obj.setProcurador(procuradorRepository.getOne(contrato.getProcurador().getId()));
		}

		if (contrato.getFiador().getId() == null) {
			obj.setFiador(null);
		} else {
			obj.setFiador(fiadorRepository.getOne(contrato.getFiador().getId()));
		}

		BeanUtils.copyProperties(obj, objSave, "id");

		return repository.save(objSave);
	}

	public Contrato isContrato(Long codigo) {
		Optional<Contrato> contratoSalvo = repository.findById(codigo);
		if (!contratoSalvo.isPresent()) {
			throw new IllegalArgumentException();
		}
		return contratoSalvo.get();
	}

	private void isImovel(Long id) {
		Imovel imovel = null;

		if (id != null) {
			imovel = imovelRepository.getOne(id);
		}

		if (imovel == null || imovel.isOcupado()) {
			throw new ImovelInexistenteOuOcupadoException();
		}
	}

	private void isLocador(Long id) {
		Locador locador = null;

		if (id != null) {
			locador = locadorRepository.getOne(id);
		}

		if (locador == null) {
			throw new LocadorInexistenteOuOcupadoException();
		}
	}

	private void isProcurador(Long id) {
		Procurador procurador = null;

		if (id != null) {
			procurador = procuradorRepository.getOne(id);
		}

		if (procurador == null) {
			throw new ProcuradorInexistenteException();
		}
	}

	private void isLocatario(Long id) {
		Locatario obj = null;

		if (id != null) {
			obj = locatarioRepository.getOne(id);
		}

		if (obj == null) {
			throw new LocatarioInexistenteOuOcupadoException();
		}
	}

	private void isFiador(Long id) {
		Fiador obj = null;

		if (id != null) {
			obj = fiadorRepository.getOne(id);
		}

		if (obj == null) {
			throw new FiadorInexistenteException();
		}
	}
}
