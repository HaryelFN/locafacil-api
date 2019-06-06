CREATE TABLE repasse (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    data_entrada DATE NOT NULL,
	data_pagamento DATE,
    despesa DECIMAL(10,2),
    info_despesa VARCHAR(1000),
    receita DECIMAL(10,2),
    info_receita VARCHAR(1000),
    referente VARCHAR(1000)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;