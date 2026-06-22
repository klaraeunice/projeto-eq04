package br.com.carteira.observer;

public class LogSegurancaObserver implements Observer {
    @Override
    public void NOTIFICAR(String evento, String mensagem) {
        System.out.println("[LOG DE SEGURANÇA] Evento: " + evento + " | Detalhes: " + mensagem);
    }
}
