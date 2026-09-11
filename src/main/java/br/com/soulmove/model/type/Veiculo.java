package br.com.soulmove.model.type;

public enum TipoVeiculo {
    CARRO("carro");



    private final String veiculo;

    TipoVeiculo(String veiculo){
        this.veiculo = veiculo;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public static TipoVeiculo getTipoVeiculo(String s){
        for (TipoVeiculo v : values()){
            if (v.getVeiculo().equalsIgnoreCase(s)){
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
