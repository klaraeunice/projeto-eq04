package br.com.carteira.model;

/**
 * Classe de modelo que representa um Usuário no sistema.
 */
public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String senha;

    // Construtor vazio necessário para frameworks de serialização (Jackson)
    public Usuario() {}

    public Usuario(Long id, String nome, String email, String cpf, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.senha = senha;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
