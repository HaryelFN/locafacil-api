package br.com.gotech.storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.SetObjectTaggingRequest;
import com.amazonaws.services.s3.model.Tag;

import br.com.gotech.config.property.LocafacilApiProperty;
import net.coobird.thumbnailator.Thumbnails;

@Component
public class S3 {

	private static final Logger logger = LoggerFactory.getLogger(S3.class);

	@Autowired
	private LocafacilApiProperty property;

	@Autowired
	private AmazonS3 amazonS3;

	public String salvarTemporariamente(String folder, MultipartFile arquivo, boolean isThumbnail) throws IOException {

		String nomeUnico = "";

		AccessControlList acl = new AccessControlList();
		acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(arquivo.getContentType());
		objectMetadata.setContentLength(arquivo.getSize());

		nomeUnico = gerarNomeUnico(arquivo.getOriginalFilename());

		try {

			ByteArrayOutputStream os1 = new ByteArrayOutputStream();
			Thumbnails.of(arquivo.getInputStream()).size(640, 480).toOutputStream(os1);
			byte[] array1 = os1.toByteArray();

			InputStream is1 = new ByteArrayInputStream(array1);
			ObjectMetadata img = new ObjectMetadata();
			img.setContentType(arquivo.getContentType());
			img.setContentLength(array1.length);

			PutObjectRequest putObjectRequest = new PutObjectRequest(property.getS3().getBucket() + folder, nomeUnico,
					is1, img).withAccessControlList(acl);
			putObjectRequest.setTagging(new ObjectTagging(Arrays.asList(new Tag("expirar", "true"))));

			amazonS3.putObject(putObjectRequest);

			if (isThumbnail) {

				ByteArrayOutputStream os = new ByteArrayOutputStream();
				Thumbnails.of(arquivo.getInputStream()).size(120, 120).toOutputStream(os);
				byte[] array = os.toByteArray();

				InputStream is = new ByteArrayInputStream(array);
				ObjectMetadata thumbMetadata = new ObjectMetadata();
				thumbMetadata.setContentType(arquivo.getContentType());
				thumbMetadata.setContentLength(array.length);

				putObjectRequest = new PutObjectRequest(property.getS3().getBucket() + folder + "/resized", nomeUnico,
						is, thumbMetadata).withAccessControlList(acl);
				putObjectRequest.setTagging(new ObjectTagging(Arrays.asList(new Tag("expirar", "true"))));

				amazonS3.putObject(putObjectRequest);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("Arquivo {} enviado com sucesso para o S3.", arquivo.getOriginalFilename());
			}

			return nomeUnico;
		} catch (IOException e) {
			throw new RuntimeException("Problemas ao tentar enviar o arquivo para o S3.", e);
		}
	}

	public void salvar(String objeto, String folder, boolean isThumbnail) {
		SetObjectTaggingRequest setObjectTaggingRequest = new SetObjectTaggingRequest(
				property.getS3().getBucket() + folder, objeto, new ObjectTagging(Collections.emptyList()));
		amazonS3.setObjectTagging(setObjectTaggingRequest);

		if (isThumbnail) {
			setObjectTaggingRequest = new SetObjectTaggingRequest(property.getS3().getBucket() + folder + "/resized",
					objeto, new ObjectTagging(Collections.emptyList()));
			amazonS3.setObjectTagging(setObjectTaggingRequest);
		}
	}

	public void substituir(String objetoAntigo, String objetoNovo, String folder, boolean isThumbnail) {
		if (StringUtils.hasText(objetoAntigo)) {
			this.remover(objetoAntigo, folder, isThumbnail);
		}

		salvar(objetoNovo, folder, isThumbnail);
	}

	public void remover(String objeto, String folder, boolean isThumbnail) {
		DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(property.getS3().getBucket() + folder,
				objeto);
		amazonS3.deleteObject(deleteObjectRequest);

		if (isThumbnail) {
			deleteObjectRequest = new DeleteObjectRequest(property.getS3().getBucket() + folder + "/resized", objeto);
			amazonS3.deleteObject(deleteObjectRequest);
		}
	}

	public String configurarUrl(String objeto, String folder) {
		String url = "https" + property.getS3().getBucket() + ".s3.amazonaws.com/" + objeto;

		if (!folder.isEmpty()) {
			url = "https:" + property.getS3().getBucket() + ".s3.amazonaws.com" + folder + "/" + objeto;
		}
		return url;
	}

	private String gerarNomeUnico(String originalFilename) {
		return UUID.randomUUID().toString() + "_" + originalFilename;
	}

}
