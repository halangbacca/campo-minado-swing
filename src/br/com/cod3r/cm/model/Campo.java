package br.com.cod3r.cm.model;

import java.util.ArrayList;
import java.util.List;

public class Campo {
    private final int linha;
    private final int coluna;

    private boolean aberto = false;
    private boolean marcado = false;
    private boolean minado = false;

    private List<Campo> vizinhos = new ArrayList<>();
    private List<CampoObserver> observadores = new ArrayList<>();

    public Campo(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public void registrarObservador(CampoObserver observador) {
        observadores.add(observador);
    }

    private void notificarObservadores(CampoEvent evento) {
        observadores.stream()
                .forEach(o -> o.eventoOcorreu(this, evento));
    }

    public boolean adicionarVizinho(Campo vizinho) {
        boolean linhaDiferente = linha != vizinho.linha;
        boolean colunaDiferente = coluna != vizinho.coluna;
        boolean diagonal = linhaDiferente && colunaDiferente;

        int deltaLinha = Math.abs(linha - vizinho.linha);
        int deltaColuna = Math.abs(coluna - vizinho.coluna);
        int deltaGeral = deltaColuna + deltaLinha;

        if (deltaGeral == 1 && !diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else if (deltaGeral == 2 && diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else {
            return false;
        }
    }

    public void alternarMarcacao() {
        if (!aberto) {
            marcado = !marcado;

            if (marcado) {
                notificarObservadores(CampoEvent.MARCAR);
            } else {
                notificarObservadores(CampoEvent.DESMARCAR);
            }
        }
    }

    public boolean abrir() {
        if (!aberto && !marcado) {
            if (minado) {
                notificarObservadores(CampoEvent.EXPLODIR);
                return true;
            }

            setAberto(true);

            if (vizinhancaSegura()) {
                vizinhos.forEach(v -> v.abrir());
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean vizinhancaSegura() {
        return vizinhos.stream().noneMatch(v -> v.minado);
    }

    public boolean isMarcado() {
        return marcado;
    }

    public boolean isMinado() {
        return minado;
    }

    void setAberto(boolean aberto) {
        this.aberto = aberto;

        if (aberto) {
            notificarObservadores(CampoEvent.ABRIR);
        }
    }

    public boolean isAberto() {
        return aberto;
    }

    void minar() {
        minado = true;
    }

    boolean objetivoAlcancado() {
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }

    public int minasNaVizinhanca() {
        return (int) vizinhos.stream().filter(v -> v.minado).count();
    }

    void reiniciar() {
        aberto = false;
        minado = false;
        marcado = false;
        notificarObservadores(CampoEvent.REINICIAR);
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

}