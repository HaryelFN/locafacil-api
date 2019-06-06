package br.com.gotech.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.gotech.Main;
import br.com.gotech.dto.QtdImovelDTO;
import br.com.gotech.model.Foto;
import br.com.gotech.model.Imovel;
import br.com.gotech.repository.FotoRepository;
import br.com.gotech.repository.ImovelRepository;
import br.com.gotech.storage.S3;

@Service
public class ImovelService {

	@Autowired
	private S3 s3;

	@Autowired
	private ImovelRepository repository;

	@Autowired
	private FotoRepository fotoRepository;

	private boolean isThumbnail = true;

	public Imovel FindById(Long codigo) {

		Imovel obj = isSaved(codigo);

		obj.getFotos().forEach(f -> {
			if (StringUtils.hasText(f.getAnexo())) {
				S3 s3 = Main.getBean(S3.class);
				f.setUrlAnexo(s3.configurarUrl(f.getAnexo(), "/imoveis"));
			}
		});

		return obj;
	}

	public QtdImovelDTO getQtdImoveis() {
		int qtdLivre = repository.findBySituacao("livre").size();
		int qtdOcupado = repository.findBySituacao("ocupado").size();
		return new QtdImovelDTO(qtdLivre, qtdOcupado);
	}

	public Imovel salvar(Imovel obj) {

		Imovel objSave = repository.save(obj);

		objSave.getFotos().forEach(f -> {

			if (StringUtils.hasText(f.getAnexo())) {
				s3.salvar(f.getAnexo(), "/imoveis", isThumbnail);
			}

			fotoRepository.save(new Foto(f.getAnexo(), objSave));
		});

		return objSave;
	}

	public Imovel atualizar(Long id, Imovel obj) {

		Imovel objSave = isSaved(id);

		obj.getFotos().forEach(f -> {
			if (StringUtils.hasText(f.getAnexo())) {
				s3.salvar(f.getAnexo(), "/imoveis", isThumbnail);
			}

			fotoRepository.save(new Foto(f.getAnexo(), objSave));
		});

		BeanUtils.copyProperties(obj, objSave, "id");

		return repository.save(objSave);
	}

	public void deletar(Long id) {
		deletarFotos(id);
		repository.deleteById(id);
	}

	public void deletarFotos(Long id) {

		List<Foto> fotos = fotoRepository.findByIdImovel(id);

		fotos.forEach(f -> {
			if (f.getAnexo() != null) {
				s3.remover(f.getAnexo(), "/imoveis", isThumbnail);
				fotoRepository.deleteById(f.getId());
			}
		});
	}

	public Imovel isSaved(Long id) {
		Optional<Imovel> objSave = repository.findById(id);
		if (!objSave.isPresent()) {
			throw new IllegalArgumentException();
		}
		return objSave.get();
	}
}
