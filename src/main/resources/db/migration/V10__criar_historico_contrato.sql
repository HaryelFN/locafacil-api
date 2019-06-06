CREATE TABLE historico_contrato (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	data_inicio DATE NOT NULL,
	data_fim DATE NOT NULL,
	valor_aluguel DECIMAL(10,2) NOT NULL,
    situacao VARCHAR(20) NOT NULL,

	id_imovel BIGINT(20) NOT NULL,
    id_locador BIGINT(20) NOT NULL,
	id_procurador BIGINT(20),
    id_locatario_1 BIGINT(20) NOT NULL,
    id_locatario_2 BIGINT(20),
    id_fiador BIGINT(20),

	FOREIGN KEY (id_imovel) REFERENCES imovel (id),
    FOREIGN KEY (id_locador) REFERENCES locador (id),
	FOREIGN KEY (id_procurador) REFERENCES procurador (id),
    FOREIGN KEY (id_locatario_1) REFERENCES locatario (id),
    FOREIGN KEY (id_locatario_2) REFERENCES locatario (id),
    FOREIGN KEY (id_fiador) REFERENCES fiador (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;