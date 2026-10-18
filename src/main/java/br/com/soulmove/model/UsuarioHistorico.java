package br.com.soulmove.model;

public class UsuarioHistorico {
    private double kgC02;
    private int missoesConcluidas;
    private int trajetosLongos;

    public double getKgC02() {
        return kgC02;
    }

    public void setKgC02(double kgC02) {
        this.kgC02 = kgC02;
    }

    public int getMissoesConcluidas() {
        return missoesConcluidas;
    }

    public void setMissoesConcluidas(int missoesConcluidas) {
        this.missoesConcluidas = missoesConcluidas;
    }

    public int getTrajetosLongos() {
        return trajetosLongos;
    }

    public void setTrajetosLongos(int trajetosLongos) {
        this.trajetosLongos = trajetosLongos;
    }
}
