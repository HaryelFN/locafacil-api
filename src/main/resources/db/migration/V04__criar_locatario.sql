CREATE TABLE locatario (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    sexo VARCHAR(1) NOT NULL,
    nome VARCHAR(60) NOT NULL,
    rg VARCHAR(30) NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    nacionalidade VARCHAR(20) NOT NULL,
    estado_civil VARCHAR(20) NOT NULL,
    profissao VARCHAR(40) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(80),
    cep VARCHAR(20),
	uf VARCHAR(2),
	cidade VARCHAR(40),
    bairro VARCHAR(30),
    logradouro VARCHAR(60),
    numero VARCHAR(20),
    complemento VARCHAR(40)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
