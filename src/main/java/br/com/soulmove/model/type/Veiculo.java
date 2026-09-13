package br.com.soulmove.model.type;

public enum Veiculo {
    CARRO("carro", 0.2),
    BICICLETA("bicicleta", 0),
    TREM("trem", 0.005),
    MOTO("moto", 0.08),
    ONIBUS("onibus", 0.04),
    METRO("metro", 0.005);



    private final String veiculo;
    private final double emissao;

    Veiculo(String veiculo, double emissao){
        this.veiculo = veiculo;
        this.emissao = emissao;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public double getEmissao(){
        return emissao;
    }

    public static Veiculo getTipoVeiculo(String s){
        for (Veiculo v : values()){
            if (v.getVeiculo().equalsIgnoreCase(s)){
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
