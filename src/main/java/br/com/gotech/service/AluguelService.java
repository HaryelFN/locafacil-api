package br.com.gotech.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.gotech.dto.AvisoSMSDescDTO;
import br.com.gotech.mail.Mailer;
import br.com.gotech.model.Aluguel;
import br.com.gotech.model.Locador;
import br.com.gotech.model.Procurador;
import br.com.gotech.repository.AluguelRepository;
import br.com.gotech.repository.ContratoRepository;
import br.com.gotech.repository.LocadorRepository;
import br.com.gotech.repository.ProcuradorRepository;
import br.com.gotech.sms.PapovaiHttpClientConnector;

@Service
public class AluguelService {

	private static final Logger logger = LoggerFactory.getLogger(AluguelService.class);

	@Autowired
	private AluguelRepository repository;

	@Autowired
	private ContratoRepository contratoRepository;

	@Autowired
	private ProcuradorRepository procuradorRepository;

	@Autowired
	private LocadorRepository locadorRepository;

	@Autowired
	private Mailer mailer;

	@Scheduled(cron = "0 0 8 * * *") // repete todos os dias as 8 horas da manha.
	public void avisarSobreAlugueisVencidosEmail() {

		if (logger.isDebugEnabled()) {
			logger.debug("Preparando envio de email-s de avisos de alugueis vencidos");
		}

		List<Aluguel> alugueis = repository.findByVencidosForEmail();

		if (alugueis.isEmpty()) {
			logger.info("Sem alugueis vencidos.");
			return;
		}

		logger.info("Existem {} alugueis vencidos.", alugueis.size());

		List<Procurador> procuradores = procuradorRepository.findAll();
		List<Locador> locadores = locadorRepository.findAll();

		mailer.alugueisVencidos(alugueis, procuradores, locadores);

		logger.info("Envio de email-s concluido.");
	}

	@Scheduled(cron = "0 0 8 * * *")
	public void avisarSobreAlugueisVencidosSMS() {

		if (logger.isDebugEnabled()) {
			logger.debug("Preparando envio de SMS sobre aviso de alugueis vencidos ha 2 dias");
		}

		List<Aluguel> alugueis = repository.findByVencidosForSMS();

		System.out.println("achou " + alugueis.size());

		if (alugueis.isEmpty()) {
			logger.info("Sem alugueis vencidos ha 2 dias.");
			return;
		}

		logger.info("Existem {} alugueis vencidos ha 2 dias", alugueis.size());

		String numbers = null;
		List<Procurador> procuradores = procuradorRepository.findAll();
		List<Locador> locadores = locadorRepository.findAll();

		numbers = procuradores.stream().map(e -> e.getTelefone().replaceAll("\\D", "") + ";").reduce("",
				String::concat);
		numbers += locadores.stream().map(e -> e.getTelefone().replaceAll("\\D", "") + ";").reduce("", String::concat);

		PapovaiHttpClientConnector papovaiHttpClientConnector = new PapovaiHttpClientConnector();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();

		try {
			papovaiHttpClientConnector.send(numbers, "Atenção existem alugueis em atraso. Sistema Locafacil",
					dateFormat.format(date).toString());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		logger.info("Envio de SMS concluido.");
	}

	@Scheduled(cron = "0 0 10 * * *") // DISPARO AS 10 HRS DA MANHA
	public void avisarSobreDesAlugueisLoc() {

		if (logger.isDebugEnabled()) {
			logger.debug("Preparando envio de SMS para locatarios, sobre descontos no aluguel");
		}

		List<AvisoSMSDescDTO> listDTO = repository.getAvisoSMSDescDTO();

		if (listDTO.isEmpty()) {
			logger.info("Não há alugueis vencendo hoje.");
			return;
		}

		PapovaiHttpClientConnector papovaiHttpClientConnector = new PapovaiHttpClientConnector();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();

		listDTO.forEach(obj -> {
			try {
				papovaiHttpClientConnector.send(obj.getTelefone(),
						"Ola " + obj.getNome().split(" ")[0]
								+ ", não perca o desconto de 10% no aluguel. Pagando até hoje, data "
								+ obj.getDataVencimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
								+ ". Se você já pagou, por favor, desconsidere este aviso.",
						dateFormat.format(date).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		logger.info("Envio de SMS concluido.");

// 		ENVIO DE EMAILS		
//		if (logger.isDebugEnabled()) {
//			logger.debug("Preparando envio de email-s para locatarios, sobre descontos no aluguel");
//		}
//
//		List<AvisoAluguelLocDTO> avisos = repository.avisoDescAluguelLoc();
//
//		if (avisos.isEmpty()) {
//			logger.info("Não há alugueis vencendo hoje.");
//			return;
//		}
//
//		logger.info("Existem {} alugueis vencendo hoje.", avisos.size());
//
//		mailer.avisoDesAluguelLoc(avisos);
//
//		logger.info("Envio de email-s concluido.");
	}

	public Aluguel atualizar(Long id, Aluguel aluguel) {

		Aluguel aluguelSalvo = isAluguel(id);

		BeanUtils.copyProperties(aluguel, aluguelSalvo, "id");
		return repository.save(aluguelSalvo);
	}

	public Aluguel isAluguel(Long id) {
		Optional<Aluguel> objSave = repository.findById(id);
		if (!objSave.isPresent()) {
			throw new IllegalArgumentException();
		}
		return objSave.get();
	}
}
