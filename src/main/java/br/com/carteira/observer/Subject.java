package br.com.carteira.observer;

public interface Subject {
    void registrarInscrito(Observer observer);
    void removerInscrito(Observer observer);
    void notificarInscritos(String evento, String mensagem);
}
