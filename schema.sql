-- Criação das Tabelas Iniciais para o Sistema de Carteira Financeira

CREATE TABLE Usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE ContaBancaria (
    id SERIAL PRIMARY KEY,
    usuario_id INT REFERENCES Usuario(id) ON DELETE CASCADE,
    nome VARCHAR(50) NOT NULL,
    saldo_atual DECIMAL(10, 2) DEFAULT 0.0
);

CREATE TABLE Transacao (
    id SERIAL PRIMARY KEY,
    conta_id INT REFERENCES ContaBancaria(id) ON DELETE CASCADE,
    valor DECIMAL(10, 2) NOT NULL,
    data_transacao DATE NOT NULL,
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('ENTRADA', 'SAIDA')),
    descricao VARCHAR(255)
);

CREATE TABLE CartaoCredito (
    id SERIAL PRIMARY KEY,
    conta_id INT REFERENCES ContaBancaria(id) ON DELETE CASCADE,
    nome VARCHAR(50) NOT NULL,
    dia_fechamento INT NOT NULL,
    dia_vencimento INT NOT NULL
);

CREATE TABLE Fatura (
    id SERIAL PRIMARY KEY,
    cartao_id INT REFERENCES CartaoCredito(id) ON DELETE CASCADE,
    mes_referencia VARCHAR(7) NOT NULL, -- Formato: YYYY-MM
    valor_total DECIMAL(10, 2) DEFAULT 0.0,
    status VARCHAR(20) NOT NULL CHECK (status IN ('ABERTA', 'FECHADA', 'PAGA'))
);
-- Criação da tabela de usuários
-- Nota: O tipo SERIAL é comum no PostgreSQL para auto-incremento.
-- Se estiver usando MySQL, utilize 'id INT AUTO_INCREMENT PRIMARY KEY'.

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS contas_bancarias (
    id SERIAL PRIMARY KEY,
    usuario_id INT NOT NULL,
    numero_conta VARCHAR(20) NOT NULL UNIQUE,
    saldo_atual DECIMAL(15, 2) NOT NULL DEFAULT 0.00, -- Removi o CHECK (saldo_atual >= 0) temporariamente caso a conta possa ficar negativada (especial)
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transacoes (
    id SERIAL PRIMARY KEY,
    conta_id INT NOT NULL,
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('ENTRADA', 'SAIDA')), -- Usando VARCHAR com CHECK (Muito mais fácil de tratar no Java!)
    valor DECIMAL(15, 2) NOT NULL CHECK (valor > 0),
    data_transacao DATE NOT NULL,
    descricao VARCHAR(255),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_conta FOREIGN KEY (conta_id) REFERENCES contas_bancarias(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_contas_usuario ON contas_bancarias(usuario_id);
CREATE INDEX IF NOT EXISTS idx_transacoes_conta ON transacoes(conta_id);
