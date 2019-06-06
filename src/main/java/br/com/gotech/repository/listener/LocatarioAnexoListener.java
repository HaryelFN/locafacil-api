package br.com.gotech.repository.listener;

import javax.persistence.PostLoad;

import org.springframework.util.StringUtils;

import br.com.gotech.Main;
import br.com.gotech.model.Locatario;
import br.com.gotech.storage.S3;

public class LocatarioAnexoListener {

//	@PostLoad
//	public void postLoad(Locatario locatario) {
//		if (StringUtils.hasText(locatario.getAnexo())) {
//			S3 s3 = Main.getBean(S3.class);
//			locatario.setUrlAnexo(s3.configurarUrl(locatario.getAnexo(), "/locatarios"));
//		}
//	}

}
