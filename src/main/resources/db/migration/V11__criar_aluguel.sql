CREATE TABLE aluguel (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    data_vencimento DATE NOT NULL,
	data_pagamento DATE,
	valor DECIMAL(10,2) NOT NULL,
    desconto DECIMAL(10,2),
    total DECIMAL(10,2),
    caucao VARCHAR(1),
    id_contrato BIGINT(20) NOT NULL,
    FOREIGN KEY (id_contrato) REFERENCES contrato (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;