CREATE TABLE
    curso (
        id BIGINT NOT null AUTO_INCREMENT,
        nome VARCHAR(255) NOT NULL,
        categoria VARCHAR(255) NOT NULL,
        PRIMARY KEY (id)
    );

CREATE TABLE
    perfil (
        id BIGINT NOT NULL AUTO_INCREMENT,
        nome VARCHAR(255) NOT NULL,
        PRIMARY KEY (id)
    );

CREATE TABLE
    usuario (
        id BIGINT NOT NULL AUTO_INCREMENT,
        nome VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL,
        senha VARCHAR(16) NOT NULL,
        PRIMARY KEY (id)
    );

CREATE TABLE
    usuario_perfil (
        usuario_id BIGINT NOT NULL,
        perfil_id BIGINT NOT NULL,
        PRIMARY KEY (usuario_id, perfil_id),
        CONSTRAINT fk_usuario_perfil_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id) ON DELETE CASCADE,
        CONSTRAINT fk_usuario_perfil_perfil FOREIGN KEY (perfil_id) REFERENCES perfil (id) ON DELETE CASCADE
    );

CREATE TABLE
    topicos (
        id BIGINT NOT NULL AUTO_INCREMENT,
        titulo VARCHAR(255) NOT NULL,
        mensagem TEXT NOT NULL,
        mensagem_hash VARCHAR(64) AS (SHA2 (CONCAT (titulo, '||', mensagem), 256)) STORED,
        data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        status VARCHAR(50) NOT NULL DEFAULT 'ABERTO',
        autor_id BIGINT NOT NULL,
        curso_id BIGINT NOT NULL,
        PRIMARY KEY (id),
        CONSTRAINT fk_topicos_autor FOREIGN KEY (autor_id) REFERENCES usuario (id) ON DELETE RESTRICT,
        CONSTRAINT fk_topicos_curso FOREIGN KEY (curso_id) REFERENCES curso (id) ON DELETE RESTRICT,
        CONSTRAINT uk_topicos_hash UNIQUE (mensagem_hash) -- Hash é VARCHAR, funciona!
    );

CREATE TABLE
    resposta (
        id BIGINT NOT NULL AUTO_INCREMENT,
        mensagem TEXT NOT NULL,
        topico_id BIGINT NOT NULL,
        data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        autor_id BIGINT NOT NULL,
        solucao TINYINT (1) DEFAULT 0,
        PRIMARY KEY (id),
        CONSTRAINT fk_resposta_topico FOREIGN KEY (topico_id) REFERENCES topicos (id) ON DELETE CASCADE,
        CONSTRAINT fk_resposta_autor FOREIGN KEY (autor_id) REFERENCES usuario (id) ON DELETE RESTRICT
    );