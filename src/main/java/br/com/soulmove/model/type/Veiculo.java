package br.com.soulmove.model.type;

public enum Veiculo {
    CARRO("carro", 0),
    BICICLETA("bicicleta", 0),
    TREM("trem", 0),
    MOTO("moto", 0),
    ONIBUS("onibus", 0),
    METRO("metro", 0);



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
