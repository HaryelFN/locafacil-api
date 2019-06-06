package br.com.gotech.resource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gotech.model.Aluguel;
import br.com.gotech.model.Locador;
import br.com.gotech.model.Procurador;
import br.com.gotech.repository.AluguelRepository;
import br.com.gotech.repository.LocadorRepository;
import br.com.gotech.repository.ProcuradorRepository;
import br.com.gotech.sms.PapovaiHttpClientConnector;

@RestController
@RequestMapping("/sms")
public class EnvioRespurce {

	@Autowired
	private AluguelRepository repository;
	
	@Autowired
	private LocadorRepository locadorRepository;
	
	@Autowired
	private ProcuradorRepository procuradorRepository;

	@GetMapping
	public void teste() {
		
		System.out.println("buscando alugueis..");

		List<Aluguel> alugueis = repository.findByVencidosForSMS();

		System.out.println("achou " + alugueis.size());
		
		if (alugueis.isEmpty()) {
			System.out.println("sem alugues vencidos ha 3 dias");
			return;
		}

		String numbers = null;
		List<Procurador> procuradores = procuradorRepository.findAll();
		List<Locador> locadores = locadorRepository.findAll();
		
		numbers = procuradores.stream().map(e -> e.getTelefone().replaceAll("\\D", "") + ";").reduce("", String::concat);
		numbers += locadores.stream().map(e -> e.getTelefone().replaceAll("\\D", "") + ";").reduce("", String::concat);

		PapovaiHttpClientConnector papovaiHttpClientConnector = new PapovaiHttpClientConnector();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();

		try {
			papovaiHttpClientConnector.send(numbers, "Atenção existem alugueis em atraso. Sistema Locafacil", dateFormat.format(date).toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}