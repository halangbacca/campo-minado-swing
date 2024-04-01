package br.com.cod3r.cm.model;

public class ResultadoEvent {
    private final boolean ganhou;

    public boolean isGanhou() {
        return ganhou;
    }

    public ResultadoEvent(boolean ganhou) {
        this.ganhou = ganhou;
    }
}
