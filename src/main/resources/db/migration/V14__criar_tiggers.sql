DELIMITER $
 
CREATE TRIGGER after_insert_contrato AFTER INSERT ON contrato
FOR EACH ROW
BEGIN

	DECLARE i INT DEFAULT 0;

	UPDATE imovel SET imovel.situacao = 'ocupado' WHERE imovel.id = NEW.id_imovel;

    WHILE i < NEW.duracao DO

        INSERT INTO aluguel (data_vencimento, data_pagamento, valor, desconto, total, caucao, id_contrato) 
        VALUES ((SELECT DATE_ADD(NEW.dia_vencimento, INTERVAL i MONTH)), NULL, NEW.valor_aluguel, NULL, NULL, NULL, NEW.id);

        SET i = i + 1;

    END WHILE;

END$

CREATE TRIGGER after_update_contrato AFTER UPDATE ON contrato
FOR EACH ROW
BEGIN

    DECLARE i INT DEFAULT 0;

    DELETE FROM aluguel WHERE aluguel.id_contrato = OLD.id;

    WHILE i < NEW.duracao DO

        INSERT INTO aluguel (data_vencimento, data_pagamento, valor, desconto, total, caucao, id_contrato) 
        VALUES ((SELECT DATE_ADD(NEW.dia_vencimento, INTERVAL i MONTH)), NULL, NEW.valor_aluguel, NULL, NULL, NULL, NEW.id); 

        SET i = i + 1;

    END WHILE;

    IF((SELECT DATEDIFF (CURDATE(), OLD.data_inicio)) > 1) THEN
        INSERT INTO historico_contrato (data_inicio, data_fim, valor_aluguel, situacao, id_imovel, id_locador, id_procurador, id_locatario_1, id_locatario_2, id_fiador) 
        VALUES (OLD.data_inicio, OLD.data_fim, OLD.valor_aluguel, 'renovado', OLD.id_imovel, OLD.id_locador, OLD.id_procurador, OLD.id_locatario_1, OLD.id_locatario_2, OLD.id_fiador);
    END IF;

END$

CREATE TRIGGER before_delete_contrato BEFORE DELETE ON contrato
FOR EACH ROW
BEGIN
	UPDATE imovel SET imovel.situacao = 'livre' WHERE imovel.id = OLD.id_imovel;

    DELETE FROM aluguel WHERE aluguel.id_contrato = OLD.id;

    IF((SELECT DATEDIFF (CURDATE(), OLD.data_inicio)) > 2) THEN
        INSERT INTO historico_contrato (data_inicio, data_fim, valor_aluguel, situacao, id_imovel, id_locador, id_procurador, id_locatario_1, id_locatario_2, id_fiador) 
        VALUES (OLD.data_inicio, (SELECT CURDATE()), OLD.valor_aluguel, 'revogado', OLD.id_imovel, OLD.id_locador, OLD.id_procurador, OLD.id_locatario_1, OLD.id_locatario_2, OLD.id_fiador);
    END IF;

END$

CREATE TRIGGER after_insert_despesa_imovel AFTER INSERT ON despesa_imovel
FOR EACH ROW
BEGIN
    INSERT INTO repasse (data_entrada, data_pagamento, despesa, info_despesa, receita, info_receita, referente) VALUES (NEW.data_criacao, null, NEW.valor, 'Manuntenção/Reforma', null, null,  (SELECT CONCAT('Referente a despesa do imóvel, situado na ', i.logradouro, ', ', i.bairro, ', ', i.cidade, ', ', i.uf) FROM imovel i WHERE i.id = NEW.id_imovel));
END$

CREATE TRIGGER after_update_aluguel AFTER UPDATE ON aluguel
FOR EACH ROW
BEGIN
    IF(NEW.data_pagamento IS NOT NULL) THEN
        -- VERIFICANDO A EXISTENCIA DE PROURADOR
        IF((SELECT COUNT(c.id_procurador) FROM aluguel a INNER JOIN contrato c ON c.id = a.id_contrato WHERE a.id = NEW.id) > 0) THEN
            -- VERIFICANDO SE E O 1° CONTRATO DESSE PROCURADOR
            IF((SELECT COUNT(*) FROM historico_contrato hc WHERE hc.id_imovel = (SELECT c.id_imovel FROM contrato c INNER JOIN aluguel a ON a.id_contrato = c.id WHERE a.id = NEW.id)) = 0) THEN

                    -- VERIFICANDO SE E O 1° ALUGUEL DESSE CONTRATO
                    IF((SELECT COUNT(a.data_pagamento) FROM aluguel a WHERE a.id_contrato = NEW.id_contrato) = 1) THEN
                        -- SE 1° ALUGUEL, RECEBE INTEGRAL
                        INSERT INTO repasse (data_entrada, data_pagamento, despesa, info_despesa, receita, info_receita, referente) 
                        VALUES (NEW.data_pagamento, null, NEW.total, 'Taxa administrativa e o 1° aluguel', 0, NULL, (SELECT CONCAT('Recebimento do aluguel do imóvel, situado na ', i.logradouro, ', ', i.bairro, ', ', i.cidade, ', ', i.uf) FROM imovel i INNER JOIN contrato c ON c.id = NEW.id_contrato INNER JOIN imovel ON imovel.id = c.id_imovel WHERE i.id = c.id_imovel));
                    ELSE
                        -- RECEBIMENTO NORMAL, 10% PARA PROCURADOR
                        INSERT INTO repasse (data_entrada, data_pagamento, despesa, info_despesa, receita, info_receita, referente) 
                        VALUES (NEW.data_pagamento, null, (NEW.total * (10 / 100)), 'Taxa administrativa e de 10%', (NEW.total - (NEW.total * (10 / 100))), 'Valor aluguel', (SELECT CONCAT('Recebimento do aluguel do imóvel, situado na ', i.logradouro, ', ', i.bairro, ', ', i.cidade, ', ', i.uf) FROM imovel i INNER JOIN contrato c ON c.id = NEW.id_contrato INNER JOIN imovel ON imovel.id = c.id_imovel WHERE i.id = c.id_imovel)); 
                    END IF;

            ELSE
                -- REGRA DO 1° ALUGUEL PARA O PROCURADOR, SE O ULTIMO CONTRATO DUROU MAIS DE 12 MESES
                IF((SELECT (SELECT TIMESTAMPDIFF(MONTH, hc.data_inicio , hc.data_fim)) AS qtd FROM historico_contrato hc WHERE hc.id_imovel = (SELECT i.id from aluguel a  INNER JOIN contrato c ON c.id = a.id_contrato INNER JOIN imovel i ON i.id = c.id_imovel WHERE a.id = NEW.id) ORDER BY hc.id ASC LIMIT 1) >= 12) THEN 
                    
                    -- VERIFICANDO SE E O 1° ALUGUEL DESSE CONTRATO
                    IF((SELECT COUNT(a.data_pagamento) FROM aluguel a WHERE a.id_contrato = NEW.id_contrato) = 1) THEN
                        -- SE 1° ALUGUEL, RECEBE INTEGRAL
                        INSERT INTO repasse (data_entrada, data_pagamento, despesa, info_despesa, receita, info_receita, referente) 
                        VALUES (NEW.data_pagamento, NULL, NEW.total, 'Taxa administrativa e o 1° aluguel', 0, NULL, (SELECT CONCAT('Recebimento do aluguel do imóvel, situado na ', i.logradouro, ', ', i.bairro, ', ', i.cidade, ', ', i.uf) FROM imovel i INNER JOIN contrato c ON c.id = NEW.id_contrato INNER JOIN imovel ON imovel.id = c.id_imovel WHERE i.id = c.id_imovel));
                    ELSE
                        -- RECEBIMENTO NORMAL, 10% PARA PROCURADOR
                        INSERT INTO repasse (data_entrada, data_pagamento, despesa, info_despesa, receita, info_receita, referente) 
                        VALUES (NEW.data_pagamento, null, (NEW.total * (10 / 100)), 'Taxa administrativa e de 10%', (NEW.total - (NEW.total * (10 / 100))), 'Valor Aluguel', (SELECT CONCAT('Recebimento do aluguel do imóvel, situado na ', i.logradouro, ', ', i.bairro, ', ', i.cidade, ', ', i.uf) FROM imovel i INNER JOIN contrato c ON c.id = NEW.id_contrato INNER JOIN imovel ON imovel.id = c.id_imovel WHERE i.id = c.id_imovel));
                    END IF;

                ELSE
                    -- RECEBIMENTO NORMAL, 10%
                    INSERT INTO repasse (data_entrada, data_pagamento, despesa, info_despesa, receita, info_receita, referente) 
                    VALUES (NEW.data_pagamento, null, (NEW.total * (10 / 100)), 'Taxa administrativa e de 10%', (NEW.total - (NEW.total * (10 / 100))), 'Valor Aluguel', (SELECT CONCAT('Recebimento do aluguel do imóvel, situado na ', i.logradouro, ', ', i.bairro, ', ', i.cidade, ', ', i.uf) FROM imovel i INNER JOIN contrato c ON c.id = NEW.id_contrato INNER JOIN imovel ON imovel.id = c.id_imovel WHERE i.id = c.id_imovel));
                END IF;

            END IF;
        ELSE
            -- SEM PROCURADOR, O VALOR TOTAL DO ALUGUEL VAI DIRETO PARA O CAIXA
            INSERT INTO caixa (operacao, data_operacao, valor, referente) VALUES ('Receita', NEW.data_pagamento, NEW.total, (SELECT CONCAT('Recebimento do aluguel do imóvel, situado na ', i.logradouro, ', ', i.bairro, ', ', i.cidade, ', ', i.uf) FROM imovel i INNER JOIN contrato c ON c.id = NEW.id_contrato INNER JOIN imovel ON imovel.id = c.id_imovel WHERE i.id = c.id_imovel));
        END IF;
    END IF;
END$

CREATE TRIGGER after_update_repasse AFTER UPDATE ON repasse
FOR EACH ROW
BEGIN
    IF(NEW.data_pagamento IS NOT NULL) THEN

        IF(NEW.despesa IS NOT NULL) THEN
            INSERT INTO caixa (operacao, data_operacao, valor, referente) VALUES ('Despesa', NEW.data_pagamento, NEW.despesa, NEW.referente);
        END IF;
            
        IF(NEW.receita IS NOT NULL) THEN
            INSERT INTO caixa (operacao, data_operacao, valor, referente) VALUES ('Receita', NEW.data_pagamento, NEW.receita, NEW.referente);            
        END IF;

    END IF;
END$

DELIMITER ;