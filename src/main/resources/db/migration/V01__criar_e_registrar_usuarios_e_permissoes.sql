CREATE TABLE usuario (
	id BIGINT(20) PRIMARY KEY,
	nome VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	senha VARCHAR(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE permissao (
	id BIGINT(20) PRIMARY KEY,
	descricao VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE usuario_permissao (
	id_usuario BIGINT(20) NOT NULL,
	id_permissao BIGINT(20) NOT NULL,
	PRIMARY KEY (id_usuario, id_permissao),
	FOREIGN KEY (id_usuario) REFERENCES usuario(id),
	FOREIGN KEY (id_permissao) REFERENCES permissao(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO usuario (id, nome, email, senha) values (1, 'Administrador', 'admin@gmail.com', '$2a$10$tgD/nunVsoadCAtjmyR4F.1CLpkkGgPXWXqwvKURp6F.2fX2WO/kC');
INSERT INTO usuario (id, nome, email, senha) values (2, 'Administrador', 'murilo', '$2a$10$ijX.sBfBWBRezxux3axUfeOv4FBvRjvVHEVoipPxF5XrCX6ttsXf.');
INSERT INTO usuario (id, nome, email, senha) values (3, 'Usuário', 'lenita', '$2a$10$FRf6tliyEYuz6m6WfE.UYuPYslFGCyj83THfg6xpmp94juBolDmS.');


INSERT INTO permissao (id, descricao) values (1, 'ROLE_PESQUISAR_LOCADOR');
INSERT INTO permissao (id, descricao) values (2, 'ROLE_CADASTRAR_LOCADOR');
INSERT INTO permissao (id, descricao) values (3, 'ROLE_REMOVER_LOCADOR');

INSERT INTO permissao (id, descricao) values (4, 'ROLE_PESQUISAR_PROCURADOR');
INSERT INTO permissao (id, descricao) values (5, 'ROLE_CADASTRAR_PROCURADOR');
INSERT INTO permissao (id, descricao) values (6, 'ROLE_REMOVER_PROCURADOR');

INSERT INTO permissao (id, descricao) values (7, 'ROLE_PESQUISAR_LOCATARIO');
INSERT INTO permissao (id, descricao) values (8, 'ROLE_CADASTRAR_LOCATARIO');
INSERT INTO permissao (id, descricao) values (9, 'ROLE_REMOVER_LOCATARIO');

INSERT INTO permissao (id, descricao) values (10, 'ROLE_PESQUISAR_FIADOR');
INSERT INTO permissao (id, descricao) values (11, 'ROLE_CADASTRAR_FIADOR');
INSERT INTO permissao (id, descricao) values (12, 'ROLE_REMOVER_FIADOR');

INSERT INTO permissao (id, descricao) values (13, 'ROLE_PESQUISAR_IMOVEL');
INSERT INTO permissao (id, descricao) values (14, 'ROLE_CADASTRAR_IMOVEL');
INSERT INTO permissao (id, descricao) values (15, 'ROLE_REMOVER_IMOVEL');

INSERT INTO permissao (id, descricao) values (16, 'ROLE_PESQUISAR_CONTRATO');
INSERT INTO permissao (id, descricao) values (17, 'ROLE_CADASTRAR_CONTRATO');
INSERT INTO permissao (id, descricao) values (18, 'ROLE_REMOVER_CONTRATO');

INSERT INTO permissao (id, descricao) values (19, 'ROLE_PESQUISAR_ALUGUEL');
INSERT INTO permissao (id, descricao) values (20, 'ROLE_CADASTRAR_ALUGUEL');
INSERT INTO permissao (id, descricao) values (21, 'ROLE_REMOVER_ALUGUEL');

INSERT INTO permissao (id, descricao) values (22, 'ROLE_PESQUISAR_DESPESA_IMOVEL');
INSERT INTO permissao (id, descricao) values (23, 'ROLE_CADASTRAR_DESPESA_IMOVEL');
INSERT INTO permissao (id, descricao) values (24, 'ROLE_REMOVER_DESPESA_IMOVEL');

INSERT INTO permissao (id, descricao) values (25, 'ROLE_PESQUISAR_CAIXA');
INSERT INTO permissao (id, descricao) values (26, 'ROLE_CADASTRAR_CAIXA');
INSERT INTO permissao (id, descricao) values (27, 'ROLE_REMOVER_CAIXA');

INSERT INTO permissao (id, descricao) values (28, 'ROLE_PESQUISAR_REPASSE');
INSERT INTO permissao (id, descricao) values (29, 'ROLE_CADASTRAR_REPASSE');
INSERT INTO permissao (id, descricao) values (30, 'ROLE_REMOVER_REPASSE');

INSERT INTO permissao (id, descricao) values (31, 'ROLE_PESQUISAR_DASHBORD');
INSERT INTO permissao (id, descricao) values (32, 'ROLE_NOTIFICACOES');

-- admin
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 1);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 2);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 3);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 4);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 5);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 6);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 7);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 8);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 9);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 10);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 11);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 12);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 13);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 14);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 15);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 16);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 17);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 18);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 19);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 20);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 21);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 22);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 23);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 24);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 25);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 26);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 27);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 28);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 29);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 30);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 31);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 32);

-- murilo
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 1);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 2);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 3);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 4);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 5);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 6);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 7);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 8);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 9);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 10);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 11);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 12);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 13);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 14);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 15);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 16);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 17);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 18);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 19);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 20);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 21);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 22);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 23);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 24);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 25);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 26);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 27);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 28);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 29);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 30);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 31);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 32);

-- lenita
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 1);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 4);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 7);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 8);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 9);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 10);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 11);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 12);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 13);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 14);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 15);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 16);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 17);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 18);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 19);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 20);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 21);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 22);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 23);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 24);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 28);
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (3, 31);



