package br.com.cod3r.cm.model;

@FunctionalInterface
public interface CampoObserver {
    public void eventoOcorreu(Campo campo, CampoEvent evento);
}
