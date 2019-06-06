package br.com.gotech.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import br.com.gotech.model.Aluguel;
import br.com.gotech.model.Contrato;
import br.com.gotech.model.Locador;
import br.com.gotech.model.Procurador;

@Component
public class Mailer {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine thymeleaf;

	// TESTE
//	@Autowired
//	private AluguelRepository repository;
//	@EventListener
//	private void teste(ApplicationReadyEvent event) {
//		String template = "mail/aviso-alugueis-vencidos";
//
//		List<Aluguel> lista = repository.findByVencidos();
//
//		Map<String, Object> variaveis = new HashMap<>();
//		variaveis.put("alugueis", lista);
//
//		this.enviarEmail("avisolocafacil@gmail.com", Arrays.asList("haryeufernandesnascimento@gmail.com", "tomazesilva@icluod.com"), "Notificação Sistema LocaFácil", template, variaveis);
//		System.out.println("Terminado o envio de e-mail...");
//	}

	public void alugueisVencidos(List<Aluguel> alugueis, List<Procurador> procuradores, List<Locador> locadores) {

		String template = "mail/aviso-alugueis-vencidos";

		Map<String, Object> variaveis = new HashMap<>();
		variaveis.put("alugueis", alugueis);

		List<String> emails = new ArrayList<>();

		locadores.forEach(l -> {
			if (l.getEmail() != null) {
				emails.add(l.getEmail());
			}
		});

		procuradores.forEach(p -> {
			if (p.getEmail() != null) {
				emails.add(p.getEmail());
			}
		});
		this.enviarEmail("avisolocafacil@gmail.com", emails, "Notificação Sistema LocaFácil - Alugueis", template,
				variaveis);
	}

	// TESTE
//	@Autowired
//	private ContratoRepository repository;
//	@EventListener
//	private void teste(ApplicationReadyEvent event) {
//		String template = "mail/aviso-contratos-vencidos";
//
//		List<Contrato> contratos = repository.findContratoVencido();
//
//		Map<String, Object> variaveis = new HashMap<>();
//		variaveis.put("contratos", contratos);
//
//		this.enviarEmail("avisolocafacil@gmail.com", Arrays.asList("haryeufernandesnascimento@gmail.com"), "Notificação Sistema LocaFácil", template, variaveis);
//		System.out.println("Terminado o envio de e-mail...");
//	}

	public void contratoVencidos(List<Contrato> contratos, List<Procurador> procuradores, List<Locador> locadores) {

		String template = "mail/aviso-contratos-vencidos";

		Map<String, Object> variaveis = new HashMap<>();
		variaveis.put("contratos", contratos);

		List<String> emails = new ArrayList<>();

		locadores.forEach(l -> {
			if (l.getEmail() != null) {
				emails.add(l.getEmail());
			}
		});

		procuradores.forEach(p -> {
			if (p.getEmail() != null) {
				emails.add(p.getEmail());
			}
		});

		this.enviarEmail("avisolocafacil@gmail.com", emails, "Notificação Sistema LocaFácil - Contratos", template,
				variaveis);
	}

	// TESTE
//	@Autowired
//	private AluguelRepository repository;
//
//	@EventListener
//	private void teste(ApplicationReadyEvent event) {
//		String template = "mail/aviso-desc-aluguel-loc";
//
//		List<AvisoAluguelLocDTO> avisos = repository.avisoDescAluguelLoc();
//
//		avisos.forEach(a -> {
//
//			Map<String, Object> variaveis = new HashMap<>();
//
//			variaveis.put("a", a);
//
//			if (a.getEmail() != null) {
//				this.enviarEmail("avisolocafacil@gmail.com", a.getEmail(), "Guimarães Imóveis", template, variaveis);
//			}
//		});
//
//		avisos = null;
//	}

//	public void avisoDesAluguelLoc(List<avis> avisos) {
//		String template = "mail/aviso-desc-aluguel-loc";
//
//		avisos.forEach(a -> {
//
//			Map<String, Object> variaveis = new HashMap<>();
//
//			variaveis.put("a", a);
//
//			if (a.getEmail() != null) {
//				this.enviarEmail("avisolocafacil@gmail.com", a.getEmail(), "Guimarães Imóveis", template, variaveis);
//			}
//		});
//
//		avisos = null;
//	}

	public void enviarEmail(String remetente, String destinatario, String assunto, String template,
			Map<String, Object> variaveis) {
		Context context = new Context(new Locale("pt", "BR"));

		variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));

		String mensagem = thymeleaf.process(template, context);

		this.enviarEmail(remetente, destinatario, assunto, mensagem);
	}

	public void enviarEmail(String remetente, String destinatario, String assunto, String mensagem) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(remetente);
			helper.setTo(destinatario);
			helper.setSubject(assunto);
			helper.setText(mensagem, true);

			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException("Problemas com o envio de e-mail!", e);
		}
	}

	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String template,
			Map<String, Object> variaveis) {
		Context context = new Context(new Locale("pt", "BR"));

		variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));

		String mensagem = thymeleaf.process(template, context);

		this.enviarEmail(remetente, destinatarios, assunto, mensagem);
	}

	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(remetente);
			helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			helper.setSubject(assunto);
			helper.setText(mensagem, true);

			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException("Problemas com o envio de e-mail!", e);
		}
	}

}
