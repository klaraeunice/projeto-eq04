package br.com.carteira.observer;

import java.util.ArrayList;
import java.util.List;

public class NotificacaoService implements Subject {
    private final List<Observer> inscritos = new ArrayList<>();

    @Override
    public void registrarInscrito(Observer observer) {
        inscritos.add(observer);
    }

    @Override
    public void removerInscrito(Observer observer) {
        inscritos.remove(observer);
    }

    @Override
    public void notificarInscritos(String evento, String mensagem) {
        for (Observer inscrito : inscritos) {
            inscrito.NOTIFICAR(evento, mensagem);
        }
    }
}
