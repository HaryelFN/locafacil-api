CREATE TABLE despesa_imovel (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    data_criacao DATE NOT NULL,
	valor DECIMAL(10,2) NOT NULL,
    descricao VARCHAR(1000) NOT NULL,
    id_imovel BIGINT(20) NOT NULL,
    FOREIGN KEY (id_imovel) REFERENCES imovel (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;