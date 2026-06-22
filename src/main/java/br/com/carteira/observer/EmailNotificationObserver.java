package br.com.carteira.observer;

public class EmailNotificationObserver implements Observer {
    @Override
    public void NOTIFICAR(String evento, String mensagem) {
        System.out.println("[NOTIFICAÇÃO EMAIL] Enviando e-mail para o usuário sobre: " + evento);
    }
}
